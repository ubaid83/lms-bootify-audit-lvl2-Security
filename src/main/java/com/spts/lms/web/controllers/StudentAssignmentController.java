package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.AssignmentConfiguration;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.assignment.StudentAssignmentQuestion;
import com.spts.lms.beans.assignment.StudentAssignmentQuestionwiseMarks;
import com.spts.lms.beans.copyleaksAudit.CopyleaksAudit;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.group.Groups;
import com.spts.lms.beans.message.StudentMessage;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.plagscan.CopyLeaks;
import com.spts.lms.plagscan.PlagScanner;
import com.spts.lms.services.assignment.AssignmentConfigurationService;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentAuditService;
import com.spts.lms.services.assignment.StudentAssignmentQuestionService;
import com.spts.lms.services.assignment.StudentAssignmentQuestionwiseMarksService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.copyleaksAudit.CopyleaksAuditService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.dashboard.DashboardService;
import com.spts.lms.services.group.GroupService;
import com.spts.lms.services.message.StudentMessageService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.user.UserRoleService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.variables.LmsVariablesService;
import com.spts.lms.web.helper.CopyCaseHelper;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.HtmlValidation;
import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;

@Controller
@SessionAttributes("userId")
public class StudentAssignmentController extends BaseController {

	@Autowired
	ApplicationContext act;
	@Autowired
	StudentMessageService studentMessageService;
	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	StudentAssignmentAuditService studentAssignmentAuditService;

	@Autowired
	ProgramService programService;

	@Autowired
	UserService userService;

	@Autowired
	CourseService courseService;

	@Autowired
	GroupService groupService;

	@Autowired
	DashboardService dashBoardService;

	@Autowired
	CopyLeaks copyLeaks;

	@Autowired
	CopyleaksAuditService copyleaksAuditService;

	@Autowired
	CopyCaseHelper copyCaseHelper;

	@Autowired
	UserRoleService userRoleService;

	@Value("${lms.assignment.submissionFolderS3}")
	private String submissionFolder;
	
	@Value("${lms.assignment.submissionFolder}")
	private String submissionFolderLocal;

	@Value("${lms.assignment.questionFolderS3}")
	private String assignmentFolder;

	@Value("${max_page_size:50}")
	private int max_page_size;

	@Value("${workStoreDir:''}")
	private String workDir;

	@Autowired
	PlagScanner submit;

	@Value("${plagiarismCheck:''}")
	private String plagiarismCheck;

	@Value("${copyleaskKey:''}")
	private String copyleaskKey;

	@Value("${copyleaksId:''}")
	private String copyleaksId;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;
	
	@Value("${app}")
	private String app;
	
	@Value("${file.base.directory}")
	private String baseDir;
	
	@Autowired
	Notifier notifier;	

	@Autowired
	AmazonS3ClientService amazonS3ClientService;
	
	@Autowired
	AssignmentConfigurationService assignmentConfigurationService;
	
	@Autowired
	LmsVariablesService lmsVariablesService;
	
	@Autowired
	StudentAssignmentQuestionwiseMarksService studentAssignmentQuestionwiseMarksService;
	
	@Autowired
	StudentAssignmentQuestionService studentAssignmentQuestionService;

	private static final Logger logger = Logger.getLogger(StudentAssignmentController.class);

	@ModelAttribute("programs")
	public List<Program> getPrograms() {
		return programService.findAllActive();
	}
	@Secured({ "ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/assignmentList", method = { RequestMethod.GET, RequestMethod.POST })
	public String assignmentList(@RequestParam(required = false) Long courseId, Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("assignmentList", "Assignments", true, false));
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		List<Assignment> assignments = Collections.emptyList();

		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			if (courseId != null) {
				assignments = assignmentService.findByFacultyAndCourseActive(username, courseId);
			} else {
				assignments = assignmentService.findAllAssignmentsByFaculty(username);
			}
		} else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			if (courseId != null) {
				assignments = assignmentService.findByUserAndCourse(username, courseId);
			} else {
				// assignments = assignmentService.findAllAssignments(username);
				assignments = assignmentService.findByUser(username);
			}
		}
		for (Assignment a : assignments) {
			Course c = courseService.findByID(a.getCourseId());
			a.setCourseName(c.getCourseName());
			assignments.set(assignments.indexOf(a), a);
		}
		if (assignments.size() == 0 || assignments.isEmpty()) {
			setNote(m, "No Assignments Allocated to you!");
		}
		m.addAttribute("courseId", courseId);
		m.addAttribute("assignments", assignments);
		m.addAttribute("rowCount", assignments.size());
		/*
		 * m.addAttribute("announcmentList", dashBoardService
		 * .listOfAnnouncementsForCourseList(username, null));
		 * m.addAttribute("toDoList", dashBoardService.getToDoList(username));
		 */
		return "assignment/assignmentList";
		// return "assignment/assignmentListNew";
	}
	@Secured({ "ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/submitAssignmentForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String submitAssignmentForm(@RequestParam(required = true) long id, Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment", false, false, true, true, false));

		Assignment assignment = assignmentService.findByID(id);
		assignment.setCourse(courseService.findByID(assignment.getCourseId()));

		StudentAssignment assignmentSubmission = studentAssignmentService.findAssignmentSubmission(username, id);

		List<StudentAssignment> studentAssignmentListForGroup = new ArrayList<>();

		if ("Y".equals(assignment.getSubmitByOneInGroup())) {

			StudentAssignment submitterOfAssignment = studentAssignmentService
					.getSubmitterForAssignment(assignment.getId(), assignmentSubmission.getGroupId());

			if (submitterOfAssignment != null) {

				if (submitterOfAssignment.getUsername().equals(username)) {

					m.addAttribute("isSubmitter", true);
					studentAssignmentListForGroup = studentAssignmentService
							.findOneAssignmentByGroupId(assignment.getId(), assignmentSubmission.getGroupId());

					if (!"Y".equals(assignmentSubmission.getSubmissionStatus())) {
						setNote(m,
								"You are the Submitter of this assignment for group.Please Submit An assignment for all in the group."
										+ " Submitted File has to be in zip format.Please Keep File Name as Student's SAPID in ZIP"
										+ " otherwise file won't get submitted ");
					} else {

						setSuccess(m, "Assignment Is Submitted By You For Group");
					}
				} else {
					m.addAttribute("isSubmitter", false);

					if (!"Y".equals(assignmentSubmission.getSubmissionStatus())) {
						setNote(m,
								"This  assignment is a group assignment.This will be submitted by "
										+ submitterOfAssignment.getStudentName() + " in your group please submit it to "
										+ submitterOfAssignment.getStudentName());
					} else {
						setSuccess(m, "Your Assignment is Submitted By " + submitterOfAssignment.getStudentName()
								+ " in a group.");
					}
				}
				m.addAttribute("isSubmittedByOneInGroup", true);
			} else {
				m.addAttribute("isSubmitter", false);
				m.addAttribute("isSubmittedByOneInGroup", false);
			}

		}

		Date dt = Utils.getInIST();
		String date = DateFormatUtils.format(dt, "yyyy-MM-dd HH:mm:SS");

		if ((assignmentSubmission.getStartDate()).compareTo(date) <= 0) {
			m.addAttribute("submission", true);

		} else {
			m.addAttribute("submission", false);
		}
		if (assignment.getRunPlagiarism() != null) {
			if (assignment.getRunPlagiarism().equals("Manual")) {
				m.addAttribute("showCheckForPlagiarism", true);
			}
		}

		m.addAttribute("assignmentSubmission", assignmentSubmission);
		m.addAttribute("assignment", assignment);
		m.addAttribute("studentAssignmentListForGroup", studentAssignmentListForGroup);

		return "assignment/submitAssignment";
	}
	@Secured({ "ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/submitAssignment", method = {

			RequestMethod.GET, RequestMethod.POST })
	public String submitAssignment(

			@ModelAttribute StudentAssignment assignmentSubmission,

			@RequestParam("file") MultipartFile file, Model m, Principal principal, RedirectAttributes rd) throws FileNotFoundException, IOException, NoSuchAlgorithmException {

		m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment",

				false, false, true, true, false));

		Assignment assignment = assignmentService.findByID(assignmentSubmission.getId());
		if(null == file || file.isEmpty()) {
			rd.addAttribute("courseId", assignment.getCourseId());
			setError(rd, "Please select the file!");
			return "redirect:/viewAssignmentFinal";
		}else {
			Tika tika = new Tika();
			  String detectedType = tika.detect(file.getBytes());
			if (file.getOriginalFilename().contains(".")) {
				Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
				logger.info("length--->"+count);
				if (count > 1 || count == 0) {
					setError(rd, "File uploaded is invalid!");
					return "redirect:/viewAssignmentFinal";
				}else {
					String extension = FilenameUtils.getExtension(file.getOriginalFilename());
					logger.info("extension--->"+extension);
					if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
							|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
						setError(rd, "File uploaded is invalid!");
						return "redirect:/viewAssignmentFinal";
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
						} else {
							setError(rd, "File uploaded is invalid!");
							return "redirect:/viewAssignmentFinal";
						}
					}
				}
			}else {
				setError(rd, "File uploaded is invalid!");
				return "redirect:/viewAssignmentFinal";
			}
		}
		String username = principal.getName();

		assignmentSubmission.setAssignmentId(assignmentSubmission.getId());
		
		//added by akshay
		StudentAssignment studentAssignHashbkp = studentAssignmentService.getStudentAssignmentHashKey(username, assignmentSubmission.getId());
		StudentAssignment studAssgnHashKey = studentAssignmentService.getStudentAssignmentHashKey(username, assignmentSubmission.getId());
		//StudentAssignment studentAssignHashbkp = studAssgnHashKey;
		
		if(assignmentSubmission.getIsHashKeyLateSubmitted().equals("Y")) {
			logger.info("late submitted---->");
			if(null == assignmentSubmission.getLateSubmRemark() || assignmentSubmission.getLateSubmRemark().isEmpty()) {
				rd.addAttribute("courseId", assignmentSubmission.getCourseId());
				setError(rd, "Reason Of Late Sumission required");
				return "redirect:/viewAssignmentFinal";
			}
			
			Date keyGenDt= Utils.getInIST();
			
			if(assignmentSubmission.getHashKey()!=null && !assignmentSubmission.getHashKey().isEmpty()) {
				logger.info("late hash key---->");
				if(studAssgnHashKey!=null) {
					studAssgnHashKey.setHashKey(assignmentSubmission.getHashKey());
					studAssgnHashKey.setLastModifiedDate(keyGenDt);
					studAssgnHashKey.setIsHashKeyLateSubmitted("Y");
					studAssgnHashKey.setLateSubmRemark(assignmentSubmission.getLateSubmRemark());
					studentAssignmentService.updateStudentAssignmentHashKey(studAssgnHashKey);
					logger.info("New update hash key---->");
				}
				
			}
		}
		if("Y".equalsIgnoreCase(assignment.getIsCheckSumReq())) {
			if(studAssgnHashKey==null) {
			rd.addAttribute("courseId", assignmentSubmission.getCourseId());
			setError(rd, "Hash Key Needs To Be Generated Before Submitting An Assignment");
			return "redirect:/viewAssignmentFinal";
			}else {
				
				File folderPath = new File(submissionFolderLocal);
				if(!folderPath.exists()) {
					folderPath.mkdirs();
				}
					File submittedFile = new File(submissionFolderLocal + File.separator + file.getOriginalFilename());
					
					try (OutputStream os = new FileOutputStream(submittedFile)) {
						os.write(file.getBytes());
					}
					MessageDigest md5Digest = MessageDigest.getInstance("SHA-1");
					String hashKey = getFileChecksum(md5Digest, submittedFile);
					FileUtils.deleteQuietly(submittedFile);
					if(!hashKey.equals(studAssgnHashKey.getHashKey())){
						studentAssignmentService.updateStudentAssignmentHashKey(studentAssignHashbkp);
						rd.addAttribute("courseId", assignmentSubmission.getCourseId());
						setError(rd, "File seems to be different from the file uploaded while generating hash key, Kindly upload the same file or regenerate the hash key!!");
						
						return "redirect:/viewAssignmentFinal";
					}
				
				
				
				
			}
			
		}

		
		
		//

		assignmentSubmission.setUsername(username);

		assignmentSubmission.setSubmissionDate(Utils.getInIST());

		assignmentSubmission.setLastModifiedBy(username);

		String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

				.format(assignmentSubmission.getSubmissionDate());
		assignmentSubmission.setActive("Y");

		int chkStartDate =0;
		int chkStartAndEndDate =0;
		
		if("Y".equals(assignment.getIsCheckSumReq())) {
			Date keyGenDate = studAssgnHashKey.getKeyGenerationTime();
			assignmentSubmission.setSubmissionDate(keyGenDate);
			chkStartDate = studentAssignmentService.chkStartandEndDateOfAssignment(username, assignment.getId(),keyGenDate);
			chkStartAndEndDate = studentAssignmentService.chkStartandEndDtOfAssignment(username, assignment.getId(),keyGenDate);
		}else {
		assignmentSubmission.setSubmissionDate(Utils.getInIST());
		 chkStartDate = studentAssignmentService.chkStartandEndDateOfAssignment(username, assignment.getId());

		 chkStartAndEndDate = studentAssignmentService.chkStartandEndDtOfAssignment(username, assignment.getId());
		}

		/*
		 * if (sdf.compareTo(assignment.getEndDate()) > 0) {
		 * 
		 * assignmentSubmission.setSubmissionStatus("Y");
		 * 
		 * logger.info("submssn status ----------------->  Y"); } else {
		 * 
		 * logger.info("submssn status ----------------->  N");
		 * 
		 * assignmentSubmission.setSubmissionStatus("N"); }
		 */

		if (chkStartAndEndDate > 0) {

			assignmentSubmission.setSubmissionStatus("Y");

		} else {

//			assignmentSubmission.setSubmissionStatus("N");
			//new buffer time
//			if("Y".equals(assignment.getIsCheckSumReq())) {
				Date eDate = Utils.converFormatsDateAlt(assignment.getEndDate());
				long diff = assignmentSubmission.getSubmissionDate().getTime() - eDate.getTime();
				//logger.info("d2 time" + endTime.getTime() + "d1 time" + endTime.getTime());
				logger.info("diff is:" + diff);
				long duration = diff / (60 * 1000);
				LmsVariables assignmentEndBuferTime = lmsVariablesService.getLmsVariableBykeyword("assignmentEndBuferTime");
				
				if(duration > Long.valueOf(assignmentEndBuferTime.getValue())) {
					assignmentSubmission.setSubmissionStatus("N");
				} else {
					assignmentSubmission.setSubmissionDate(eDate);
					assignmentSubmission.setSubmissionStatus("Y");
					chkStartAndEndDate = 1;
				}
//			}
			//new buffer time
		}

		assignmentSubmission.setStartDate(assignment.getStartDate());

		assignmentSubmission.setEndDate(assignment.getEndDate());

		if (chkStartDate == 0) {
			setNote(rd, "Assignment Submission has not started yet or deadline is missed!!");

		} else {

			if (!file.isEmpty()) {
				Date date = new Date();

				String errorMessage = uploadAssignmentSubmissionFileForS3(assignmentSubmission, file);
				logger.info("ErrorMessgae " + errorMessage);

				Map<String, Integer> plagValueMap = new HashMap<String, Integer>();

				if (errorMessage == null) {

					CopyleaksAudit cplAudit = copyleaksAuditService.getRecordByUsername(username, assignment.getId());

					boolean isPresent = false;
					boolean copyleakscheck = false;
					if (cplAudit == null) {
						isPresent = false;
						copyleakscheck = true;
					} else {

						isPresent = true;
						if (cplAudit.getCount() >= 2) {
							copyleakscheck = false;

						} else {
							copyleakscheck = true;
						}

					}

					if (assignment.getPlagscanRequired().equals("Yes")
							&& assignment.getRunPlagiarism().equals("Submission") && copyleakscheck == true) {

						if (plagiarismCheck.equalsIgnoreCase("copyLeaks"))

						{ // CopyLeaks copyLeaks = new CopyLeaks();

							try {
								File storedFile = submit.multipartToFileForS3(assignmentSubmission.getStudentFilePath());
								String scanId = app.replaceAll("-", "").toLowerCase() +"-"+ assignment.getId() +"-"+ username+"-"+RandomStringUtils.randomNumeric(2, 2);
								Map<String,String> scanStatus = copyLeaks.scanFileForCopyleaks(copyleaksId,copyleaskKey,storedFile,scanId);
								if(scanStatus.containsKey("Error")){
									setError(rd,"For "+username+ ": "+scanStatus.get("Error")+"Please submit the file again.");
									if(studAssgnHashKey!=null) {
										studAssgnHashKey.setActive("N");
										studentAssignmentService.softDelStudentAssignmentHashKey(studAssgnHashKey);
									}
								}else {
										
											studentAssignmentAuditService.insert(assignmentSubmission);
											StudentAssignment auditAssignmentSubmission = studentAssignmentAuditService.findAssignmentSubmission(username,assignment.getId());
										
										logger.info("Waiting...");
										long waitingTime = 0;
										while(null == auditAssignmentSubmission.getThreshold()) {
												Thread.sleep(5000);
												waitingTime = waitingTime + 5000;
												logger.info("Waiting..."+auditAssignmentSubmission.getThreshold());
												//wait for 4.30 mins only
												if(waitingTime > 258000) {
													break;
												}
												auditAssignmentSubmission = studentAssignmentAuditService.findAssignmentSubmission(username,assignment.getId());
												logger.info("Checking..."+auditAssignmentSubmission.getThreshold());
										}
										if(null != auditAssignmentSubmission.getThreshold()) {
											logger.info("Threshold--->"+auditAssignmentSubmission.getThreshold());
											if (assignment.getThreshold() <= auditAssignmentSubmission.getThreshold()) {
												setError(rd, "Uploaded File does not fall under plagiarism threshold " + auditAssignmentSubmission.getThreshold()
														+ "% copied from " + auditAssignmentSubmission.getUrl());
												if(studAssgnHashKey!=null) {
													studAssgnHashKey.setActive("N");
													studentAssignmentService.softDelStudentAssignmentHashKey(studAssgnHashKey);
												}
											}else{
												assignmentSubmission.setThreshold(auditAssignmentSubmission.getThreshold());
												assignmentSubmission.setUrl(auditAssignmentSubmission.getUrl());
												studentAssignmentService.update(assignmentSubmission);
												if (chkStartAndEndDate == 0) {
													setNote(rd,"YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY");
												} else {
													setSuccess(rd, "Assigment Answer file uploaded successfully");
												}
											}	
										}else {
											if(studAssgnHashKey!=null) {
												studAssgnHashKey.setActive("N");
												studentAssignmentService.softDelStudentAssignmentHashKey(studAssgnHashKey);
											}
											setError(rd, "File takes too long time for plagiarism.");
										}
								}
								
//								plagValueMap = copyLeaks.scan(copyleaksId, copyleaskKey, storedFile);
//								String url = "";
//								Integer plagValue = 0;
//								Integer creditUsed = 0;
//
//								if (plagValueMap.isEmpty()) {
//									assignmentSubmission.setThreshold(0);
//									assignmentSubmission.setUrl(" ");
//								} else {
//									for (String key : plagValueMap.keySet()) {
//										url = key;
//
//										if (url != null) {
//
//										} else {
//											url = "Copyleaks internal database";
//										}
//										plagValue = plagValueMap.get(key);
//
//										if (url == "creditUsed") {
//
//											creditUsed = plagValue;
//
//										} else {
//											assignmentSubmission.setUrl(url);
//											assignmentSubmission.setThreshold(plagValue);
//										}
//
//										/*
//										 * assignmentSubmission.setUrl(url); assignmentSubmission
//										 * .setThreshold(plagValue);
//										 */
//
//									}
//								}
//
//								if (isPresent == false) {
//
//									CopyleaksAudit cpl = new CopyleaksAudit();
//									cpl.setAssignmentId(assignment.getId());
//									cpl.setUsername(username);
//									cpl.setCount(1);
//									cpl.setCreditUsed(creditUsed);
//									cpl.setCreatedBy(username);
//									cpl.setLastModifiedBy(username);
//
//									copyleaksAuditService.insert(cpl);
//
//								} else {
//
//									cplAudit.setCount(cplAudit.getCount() + 1);
//									cplAudit.setCreditUsed(cplAudit.getCreditUsed() + creditUsed);
//
//									copyleaksAuditService.update(cplAudit);
//
//								}
//
//								// logger.info("plagValue--" + plagValueMap);
//								if (assignment.getThreshold() == null) {
//									assignment.setThreshold(50);
//								} else {
//
//								}
//								if (plagValue >= assignment.getThreshold()) {
//
//									setError(rd, "Uploaded File does not fall under plag threshold " + plagValue
//											+ "% copied from " + url);
//
//								} else {
//									studentAssignmentService.update(assignmentSubmission);
//									studentAssignmentAuditService.insert(assignmentSubmission); //
//									setSuccess(rd, "Assigment Answer file uploaded successfully");
//
//									String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//
//											.format(assignmentSubmission.getSubmissionDate());
//
//									if (chkStartAndEndDate == 0) {
//
//										setNote(rd,
//
//												"YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY"
//
//										);
//									} else {
//
//										setSuccess(rd, "Assigment Answer file uploaded successfully");
//									}
//								}
								FileUtils.deleteQuietly(storedFile);
							} catch (Exception e) {
								logger.error(e.getMessage());
							}
						}

					} else {
						try {

							studentAssignmentService.update(assignmentSubmission);
							studentAssignmentAuditService.insert(assignmentSubmission); //
							setSuccess(rd, "Assigment Answer file uploaded successfully");

							String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

									.format(assignmentSubmission.getSubmissionDate());

							if (chkStartAndEndDate == 0) {
								setNote(rd,

										"YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY"

								);
							} else {
								setSuccess(rd, "Assigment Answer file uploaded successfully");
							}
						} catch (Exception e) {
							logger.error(e.getMessage());
						}

					}

				} else {
					if(studAssgnHashKey!=null) {
						studAssgnHashKey.setActive("N");
						studentAssignmentService.softDelStudentAssignmentHashKey(studAssgnHashKey);
					}
					setError(rd, errorMessage);
				}

			} else {
				setError(rd, "File is empty, please upload correct file.");
			}

		}

		if (assignment.getRunPlagiarism() != null) {
			if (assignment.getRunPlagiarism().equals("Manual")) {
				m.addAttribute("showCheckForPlagiarism", true);
			}
		}

		m.addAttribute("assignment", assignment);
		assignmentSubmission =

				studentAssignmentService.findAssignmentSubmission(username,

						assignmentSubmission.getId());

		m.addAttribute("assignmentSubmission", assignmentSubmission);

		rd.addAttribute("courseId", assignmentSubmission.getCourseId());

		return "redirect:/viewAssignmentFinal";
	}
	@Secured({ "ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/checkForPlagiarism", method = {

			RequestMethod.GET, RequestMethod.POST })
	public String checkForPlagiarism(Model m, Principal principal, @RequestParam Long saId) {
		m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment", false, false, true, true, false));

		StudentAssignment assignmentSubmission = studentAssignmentService.findByID(saId);
		Assignment assignment = assignmentService.findByID(assignmentSubmission.getAssignmentId());
		m.addAttribute("assignment", assignment);
		m.addAttribute("assignmentSubmission", assignmentSubmission);
		// PlagiarismCheck pc = new PlagiarismCheck();

		Map<String, Integer> plagValueMap = new HashMap<String, Integer>();
		if (plagiarismCheck.equalsIgnoreCase("copyLeaks"))

		{ // CopyLeaks copyLeaks = new CopyLeaks();

			try {

				File storedFile = submit.multipartToFile(assignmentSubmission.getStudentFilePath());
				plagValueMap = copyLeaks.scan(copyleaksId, copyleaskKey, storedFile);
				String url = "";
				Integer plagValue = 0;
				for (String key : plagValueMap.keySet()) {
					url = key;
					if (url != null) {

					} else {
						url = "Copyleaks internal database";
					}
					plagValue = plagValueMap.get(key);
					assignmentSubmission.setUrl(url);
					assignmentSubmission.setThreshold(plagValue);

				}
				// logger.info("plagValue--" + plagValueMap);
				if (assignment.getThreshold() == null) {
					assignment.setThreshold(50);
				} else {

				}

				if (plagValue >= assignment.getThreshold()) {

					setError(m,
							"Uploaded File does not fall under plag threshold " + plagValue + "% copied from " + url);
					studentAssignmentService.update(assignmentSubmission);

				} else {
					studentAssignmentService.update(assignmentSubmission);

					setSuccess(m, "Assigment Answer file uploaded successfully");

					String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

							.format(assignmentSubmission.getSubmissionDate());

					if (sdf1.compareTo(assignment.getEndDate()) > 0) {
						setSuccess(m,

								"YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY"

						);
					} else {
						setSuccess(m, "Assigment Answer file uploaded successfully");
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

		return "assignment/submitAssignment";

	}
	@Secured({ "ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/checkForPlagiarismAll", method = {

			RequestMethod.GET, RequestMethod.POST })
	public String checkForPlagiarismAll(Model m, Principal principal, @RequestParam Long assignmentId,RedirectAttributes rd) {
		m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment", false, false, true, true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Assignment assignment = assignmentService.findByID(assignmentId);
		m.addAttribute("assignment", assignment);
		List<StudentAssignment> students = studentAssignmentService.findStudentsForPlagiarismCheck(assignmentId);
		logger.info("students List Size--->" + students.size());
		if (plagiarismCheck.equalsIgnoreCase("copyLeaks"))

		{ // CopyLeaks copyLeaks = new CopyLeaks();

			CopyleaksAudit cplAudit = copyleaksAuditService.getRecordByUsername(username, assignmentId);

			boolean isPresent = false;
			boolean copyleakscheck = true;
			if (cplAudit == null) {
				isPresent = false;
				copyleakscheck = true;
			} else {

				isPresent = true;
//				if (cplAudit.getCount() >= 2) {
//					copyleakscheck = false;
//
//				} else {
//					copyleakscheck = true;
//				}

			}
			
			if (isPresent == false) {

				CopyleaksAudit cpl = new CopyleaksAudit();
				cpl.setAssignmentId(assignmentId);
				cpl.setUsername(username);
				cpl.setCount(0);
				cpl.setCreditUsed(0);
				cpl.setCreatedBy(username);
				cpl.setLastModifiedBy(username);

				copyleaksAuditService.insert(cpl);

			}

			Integer totalCreditUsed = 0;
			logger.info("copyleakscheck--->" + copyleakscheck);
			for (StudentAssignment sa : students) {

				Map<String, Integer> plagValueMap = new HashMap<String, Integer>();
				Long saId = sa.getId();

				StudentAssignment assignmentSubmission = studentAssignmentService.findByID(saId);

				if (copyleakscheck == true) {

					try {
						if (assignmentSubmission.getThreshold() == null) {

							File storedFile = submit.multipartToFileForS3(assignmentSubmission.getStudentFilePath());
							String scanId = app.replaceAll("-", "").toLowerCase() +"-"+ assignment.getId() +"-"+ assignmentSubmission.getUsername() + "-" + username + "-" + RandomStringUtils.randomNumeric(2, 2);
							Map<String,String> scanStatus = copyLeaks.scanFileForCopyleaks(copyleaksId,copyleaskKey,storedFile,scanId);
							if(scanStatus.containsKey("Error")){
								assignmentSubmission.setUrl(scanStatus.get("Error"));
							}else {
								assignmentSubmission.setUrl("In Progress...");
							}
//							plagValueMap = copyLeaks.scan(copyleaksId, copyleaskKey, storedFile);
//							String url = "";
//							Integer plagValue = 0;
//							Integer creditUsed = 0;
//							for (String key : plagValueMap.keySet()) {
//								url = key;
//								if (url != null) {
//
//								} else {
//									url = "Copyleaks internal database";
//								}
//
//								plagValue = plagValueMap.get(key);
//								logger.info("url---->" + url + "threshold--->" + plagValue);
//								if (url == "creditUsed") {
//
//									creditUsed = plagValue;
//									totalCreditUsed = totalCreditUsed + creditUsed;
//
//								} else {
//									assignmentSubmission.setUrl(url);
//									assignmentSubmission.setThreshold(plagValue);
//								}
//
//								/*
//								 * assignmentSubmission.setUrl(url);
//								 * assignmentSubmission.setThreshold(plagValue);
//								 */
//
//							}
//
//							// logger.info("plagValue--" + plagValueMap);
//
							studentAssignmentService.update(assignmentSubmission);
							FileUtils.deleteQuietly(storedFile);

						} 

					} catch (Exception e) {
						logger.error(e.getMessage());
					}

				}

			}

//			if (isPresent == false) {
//
//				CopyleaksAudit cpl = new CopyleaksAudit();
//				cpl.setAssignmentId(assignmentId);
//				cpl.setUsername(username);
//				cpl.setCount(1);
//				cpl.setCreditUsed(totalCreditUsed);
//				cpl.setCreatedBy(username);
//				cpl.setLastModifiedBy(username);
//
//				copyleaksAuditService.insert(cpl);
//
//			} else {
//
//				cplAudit.setCount(cplAudit.getCount() + 1);
//				cplAudit.setCreditUsed(cplAudit.getCreditUsed() + totalCreditUsed);
//
//				copyleaksAuditService.update(cplAudit);
//
//			}

		}
		/*
		 * ModelAndView mav = new ModelAndView(); mav.setViewName(evaluateByStudent(1,
		 * principal, m, assignment, assignmentId)); return mav;
		 */

		return (evaluateByStudent(1, principal, m, assignment, assignmentId));
//		rd.addAttribute("pageNo",1);
//		//rd.addAttribute("principal",principal);
//		//rd.addAttribute("m",m);
//		rd.addAttribute("assignment",assignment);
//		rd.addAttribute("id",assignmentId);
//		return "redirect:/evaluateByStudent";

	}

	/*
	 * @RequestMapping(value = "/submitAssignment", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String submitAssignment(
	 * 
	 * @ModelAttribute StudentAssignment assignmentSubmission,
	 * 
	 * @RequestParam("file") MultipartFile file, Model m, Principal principal) {
	 * m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment",
	 * false, false, true, true, false)); Assignment assignment =
	 * assignmentService.findByID(assignmentSubmission .getId()); String username =
	 * principal.getName();
	 * assignmentSubmission.setAssignmentId(assignmentSubmission.getId());
	 * assignmentSubmission.setUsername(username);
	 * assignmentSubmission.setSubmissionDate(new Date());
	 * logger.info("submssn Date----> " + assignmentSubmission.getSubmissionDate());
	 * logger.info("End date-------------------->" + assignment.getEndDate());
	 * 
	 * String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	 * .format(assignmentSubmission.getSubmissionDate());
	 * 
	 * 
	 * 
	 * Date submittedDate=sdf.parse(assignmentSubmission.getSubmissionDate().
	 * toString());
	 * logger.info("submssn Date----> "+assignmentSubmission.getSubmissionDate ());
	 * Date endDate=sdf.parse(assignment.getEndDate());
	 * 
	 * 
	 * if (sdf.compareTo(assignment.getEndDate()) < 0) {
	 * assignmentSubmission.setSubmissionStatus("Y");
	 * logger.info("submssn status ----------------->  Y"); } else {
	 * 
	 * logger.info("submssn status ----------------->  N");
	 * assignmentSubmission.setSubmissionStatus("N"); }
	 * 
	 * assignmentSubmission.setStartDate(assignment.getStartDate());
	 * assignmentSubmission.setEndDate(assignment.getEndDate());
	 * 
	 * if (!file.isEmpty()) { Date date = new Date(); // String
	 * date1=date.toString(); String errorMessage = uploadAssignmentSubmissionFile(
	 * assignmentSubmission, file); if (errorMessage == null) {
	 * studentAssignmentService.update(assignmentSubmission); setSuccess(m,
	 * "Assigment Answer file uploaded successfully");
	 * 
	 * String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	 * .format(assignmentSubmission.getSubmissionDate());
	 * 
	 * if (sdf1.compareTo(assignment.getEndDate()) > 0) { setSuccess( m,
	 * "YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY"
	 * ); }
	 * 
	 * } else { setError(m, errorMessage); } } else { setError(m,
	 * "File is empty, please upload correct file."); }
	 * 
	 * m.addAttribute("assignment", assignment); assignmentSubmission =
	 * studentAssignmentService .findAssignmentSubmission(username,
	 * assignmentSubmission.getId());
	 * 
	 * logger.info("assgn List--------->" + assignmentSubmission.getStartDate() +
	 * assignmentSubmission.getEndDate());
	 * 
	 * 
	 * m.addAttribute("assignmentSubmission", assignmentSubmission); return
	 * "assignment/submitAssignment"; }
	 */

	/*
	 * @RequestMapping(value = "/submitAssignment", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String submitAssignment(
	 * 
	 * @ModelAttribute StudentAssignment assignmentSubmission,
	 * 
	 * @RequestParam("file") MultipartFile file, Model m, Principal principal) {
	 * m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment",
	 * false, false, true, true, false)); Assignment assignment =
	 * assignmentService.findByID(assignmentSubmission .getId()); String username =
	 * principal.getName();
	 * assignmentSubmission.setAssignmentId(assignmentSubmission.getId());
	 * assignmentSubmission.setUsername(username);
	 * assignmentSubmission.setSubmissionDate(new Date());
	 * logger.info("submssn Date----> " + assignmentSubmission.getSubmissionDate());
	 * logger.info("End date-------------------->" + assignment.getEndDate());
	 * 
	 * String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	 * .format(assignmentSubmission.getSubmissionDate());
	 * 
	 * Date submittedDate=sdf.parse(assignmentSubmission.getSubmissionDate().
	 * toString());
	 * logger.info("submssn Date----> "+assignmentSubmission.getSubmissionDate ());
	 * Date endDate=sdf.parse(assignment.getEndDate());
	 * 
	 * 
	 * if (sdf.compareTo(assignment.getEndDate()) < 0) {
	 * assignmentSubmission.setSubmissionStatus("Y");
	 * logger.info("submssn status ----------------->  Y"); } else {
	 * 
	 * logger.info("submssn status ----------------->  N");
	 * assignmentSubmission.setSubmissionStatus("N"); }
	 * 
	 * assignmentSubmission.setStartDate(assignment.getStartDate());
	 * assignmentSubmission.setEndDate(assignment.getEndDate());
	 * 
	 * if (!file.isEmpty()) { Date date = new Date(); // String
	 * date1=date.toString(); String errorMessage = uploadAssignmentSubmissionFile(
	 * assignmentSubmission, file); if (errorMessage == null) {
	 * studentAssignmentService.update(assignmentSubmission); setSuccess(m,
	 * "Assigment Answer file uploaded successfully");
	 * 
	 * String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	 * .format(assignmentSubmission.getSubmissionDate());
	 * 
	 * if (sdf1.compareTo(assignment.getEndDate()) > 0) { setSuccess( m,
	 * "YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY"
	 * ); }
	 * 
	 * } else { setError(m, errorMessage); } } else { setError(m,
	 * "File is empty, please upload correct file."); }
	 * 
	 * m.addAttribute("assignment", assignment); assignmentSubmission =
	 * studentAssignmentService .findAssignmentSubmission(username,
	 * assignmentSubmission.getId());
	 * 
	 * logger.info("assgn List--------->" + assignmentSubmission.getStartDate() +
	 * assignmentSubmission.getEndDate());
	 * 
	 * 
	 * m.addAttribute("assignmentSubmission", assignmentSubmission); return
	 * "assignment/submitAssignment"; }
	 */

	// Added with copyleaks changes

	/*
	 * @RequestMapping(value = "/submitAssignment", method = {
	 * 
	 * RequestMethod.GET, RequestMethod.POST }) public String submitAssignment(
	 * 
	 * @ModelAttribute StudentAssignment assignmentSubmission,
	 * 
	 * @RequestParam("file") MultipartFile file, Model m, Principal principal) {
	 * 
	 * m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment",
	 * 
	 * false, false, true, true, false)); Assignment assignment =
	 * 
	 * assignmentService.findByID(assignmentSubmission.getId());
	 * 
	 * String username = principal.getName();
	 * 
	 * assignmentSubmission.setAssignmentId(assignmentSubmission.getId());
	 * 
	 * assignmentSubmission.setUsername(username);
	 * 
	 * assignmentSubmission.setSubmissionDate(new Date());
	 * 
	 * logger.info("submssn Date----> " +
	 * 
	 * assignmentSubmission.getSubmissionDate());
	 * 
	 * assignmentSubmission.setLastModifiedBy(username);
	 * 
	 * logger.info("End date-------------------->" + assignment.getEndDate());
	 * 
	 * String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	 * 
	 * .format(assignmentSubmission.getSubmissionDate());
	 * assignmentSubmission.setActive("Y");
	 * 
	 * if (sdf.compareTo(assignment.getEndDate()) < 0) {
	 * 
	 * assignmentSubmission.setSubmissionStatus("Y");
	 * 
	 * logger.info("submssn status ----------------->  Y"); } else {
	 * 
	 * logger.info("submssn status ----------------->  N");
	 * 
	 * assignmentSubmission.setSubmissionStatus("N"); }
	 * 
	 * assignmentSubmission.setStartDate(assignment.getStartDate());
	 * 
	 * assignmentSubmission.setEndDate(assignment.getEndDate());
	 * 
	 * int chkStartandEndDateOfTest = studentAssignmentService
	 * .chkStartandEndDateOfAssignment(username, assignment.getId());
	 * 
	 * if (chkStartandEndDateOfTest == 0) { setNote(m,
	 * "Assignment Submission has not started yet or deadline is missed!!");
	 * 
	 * } else {
	 * 
	 * if (!file.isEmpty()) { Date date = new Date();
	 * 
	 * String errorMessage = uploadAssignmentSubmissionFile( assignmentSubmission,
	 * file); Map<String, Integer> plagValueMap = new HashMap<String, Integer>();
	 * 
	 * if (errorMessage == null) { logger.info("ass sub" +
	 * assignmentSubmission.getStudentFilePath());
	 * 
	 * if (plagiarismCheck.equalsIgnoreCase("copyLeaks"))
	 * 
	 * { // CopyLeaks copyLeaks = new CopyLeaks();
	 * 
	 * try { File storedFile = submit .multipartToFile(assignmentSubmission
	 * .getStudentFilePath()); plagValueMap = copyLeaks.scan(copyleaksId,
	 * copyleaskKey, storedFile); String url = ""; Integer plagValue = 0; for
	 * (String key : plagValueMap.keySet()) { url = key; plagValue =
	 * plagValueMap.get(key);
	 * 
	 * } logger.info("plagValue--" + plagValueMap); if (assignment.getThreshold() ==
	 * null) { assignment.setThreshold(50); } else {
	 * 
	 * } if (plagValue >= assignment.getThreshold()) {
	 * 
	 * setError(m, "Uploaded File does not fall under plag threshold " + plagValue +
	 * "% copied from " + url);
	 * 
	 * } else { studentAssignmentService .update(assignmentSubmission);
	 * studentAssignmentAuditService .insert(assignmentSubmission); // setSuccess(m,
	 * "Assigment Answer file uploaded successfully");
	 * 
	 * String sdf1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss")
	 * 
	 * .format(assignmentSubmission .getSubmissionDate());
	 * 
	 * if (sdf1.compareTo(assignment.getEndDate()) > 0) { setSuccess( m,
	 * 
	 * "YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY"
	 * 
	 * ); } else { setSuccess(m, "Assigment Answer file uploaded successfully"); } }
	 * } catch (Exception e) { logger.error(e.getMessage()); } }
	 * 
	 * } else { setError(m, errorMessage); } } else { setError(m,
	 * "File is empty, please upload correct file."); }
	 * 
	 * //}
	 * 
	 * m.addAttribute("assignment", assignment); assignmentSubmission =
	 * 
	 * studentAssignmentService.findAssignmentSubmission(username,
	 * 
	 * assignmentSubmission.getId());
	 * 
	 * logger.info("assgn List--------->" + assignmentSubmission.getStartDate()
	 * 
	 * + assignmentSubmission.getEndDate());
	 * 
	 * m.addAttribute("assignmentSubmission", assignmentSubmission); return
	 * 
	 * "assignment/submitAssignment"; }
	 */

	/*
	 * public String uploadAssignmentSubmissionFile(StudentAssignment bean,
	 * 
	 * MultipartFile file) {
	 * 
	 * String errorMessage = null;
	 * 
	 * InputStream inputStream = null;
	 * 
	 * OutputStream outputStream = null;
	 * 
	 * // CommonsMultipartFile file = bean.getFileData();
	 * 
	 * String fileName = file.getOriginalFilename();
	 * 
	 * // Replace special characters in file
	 * 
	 * fileName = fileName.replaceAll("'", "_");
	 * 
	 * fileName = fileName.replaceAll(",", "_");
	 * 
	 * fileName = fileName.replaceAll("&", "and");
	 * 
	 * fileName = fileName.replaceAll(" ", "_");
	 * 
	 * fileName = fileName.substring(0, fileName.lastIndexOf("."))
	 * 
	 * + "_"
	 * 
	 * + RandomStringUtils.randomAlphanumeric(10)
	 * 
	 * + fileName.substring(fileName.lastIndexOf("."),
	 * 
	 * fileName.length());
	 * 
	 * try {
	 * 
	 * inputStream = file.getInputStream();
	 * 
	 * String filePath = submissionFolder + File.separator + fileName;
	 * 
	 * bean.setFilePath(filePath);
	 * 
	 * bean.setFilePreviewPath(filePath);
	 * 
	 * String previewPath = submissionFolder + File.separator + fileName;
	 * 
	 * bean.setStudentFilePath(filePath);
	 * 
	 * bean.setFilePreviewPath(filePath);
	 * 
	 * // Check if Folder exists which is one folder per Exam (Jun2015,
	 * 
	 * // Dec2015 etc.)
	 * 
	 * File folderPath = new File(submissionFolder);
	 * 
	 * if (!folderPath.exists()) {
	 * 
	 * folderPath.mkdirs();
	 * 
	 * }
	 * 
	 * File newFile = new File(filePath);
	 * 
	 * outputStream = new FileOutputStream(newFile);
	 * 
	 * IOUtils.copy(inputStream, outputStream);
	 * 
	 * } catch (IOException e) {
	 * 
	 * errorMessage = "Error in uploading Assignment file : "
	 * 
	 * + e.getMessage();
	 * 
	 * logger.error("Exception", e);
	 * 
	 * } finally {
	 * 
	 * IOUtils.closeQuietly(inputStream);
	 * 
	 * IOUtils.closeQuietly(outputStream);
	 * 
	 * }
	 * 
	 * return errorMessage;
	 * 
	 * }
	 */

	// 05/04/2020 shubham

	public String uploadAssignmentSubmissionFile(StudentAssignment bean,

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

			String filePath = submissionFolder + "/" + fileName;

			bean.setFilePath(filePath);

			bean.setFilePreviewPath(filePath);

			String previewPath = submissionFolder + "/" + fileName;

			bean.setStudentFilePath(filePath);

			bean.setFilePreviewPath(filePath);

			// Check if Folder exists which is one folder per Exam (Jun2015,

			// Dec2015 etc.)

			File folderPath = new File(submissionFolder);

			if (!folderPath.exists()) {

				folderPath.mkdirs();

			}

			File newFile = new File(filePath);

			outputStream = new FileOutputStream(newFile);

			IOUtils.copy(inputStream, outputStream);

			amazonS3ClientService.uploadFile(newFile, submissionFolder);

		} catch (IOException e) {

			errorMessage = "Error in uploading Assignment file : "

					+ e.getMessage();

			logger.error("Exception", e);

		} finally {

			IOUtils.closeQuietly(inputStream);

			IOUtils.closeQuietly(outputStream);

		}

		return errorMessage;

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/searchAssignmentToEvaluate", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchAssignmentToEvaluate(@RequestParam(required = false, defaultValue = "1") int pageNo,
			Principal principal, Model m, @ModelAttribute("assignment") StudentAssignment assignment,
			@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {
		m.addAttribute("webPage",
				new WebPage("evaluateAssignment", "Search Assignment To Evaluate", true, false, true, true, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);

		String acadSession = u1.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {

			if (courseId == null || courseId.isEmpty()) {
				if (request.getSession().getAttribute("courseRecord") == null
						|| request.getSession().getAttribute("courseRecord").equals("")) {

				} else {
					request.getSession().removeAttribute("courseRecord");
				}

			}
			assignment.setEvaluatedBy(username);
			// assignment.setSubmissionStatus("Y");
			List<StudentAssignment> assignmentsList = studentAssignmentService.findAssignmentForEvaluation(assignment,
					username);
			for (StudentAssignment sa : assignmentsList) {
				User u = userService.findByUserName(sa.getUsername());
				String firstName = u.getFirstname();
				String lastName = u.getLastname();
				sa.setFirstname(firstName);
				sa.setLastname(lastName);

				sa.setRollNo(u.getRollNo());
				String studentFilePath = StringUtils.trimToNull(sa.getStudentFilePath());
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
					sa.setShowFileDownload("false");
				}
				assignmentsList.set(assignmentsList.indexOf(sa), sa);

			}

			m.addAttribute("assignmentsList", assignmentsList);
			m.addAttribute("q", getQueryString(assignment));
			m.addAttribute("assignment", assignment);
			m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));

			if (assignmentsList == null || assignmentsList.size() == 0) {
				setNote(m, "No Assignments found");
			}
			

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Assignment List");
		}

		return "assignment/assignmentToEvaluateList";
	}
	@Secured({ "ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/updateStudentAssignmentForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateStudentAssignmentForm(@RequestParam Long courseId,
			@ModelAttribute StudentAssignment studentassignment, @RequestParam(required = false) Long id, Model m,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment", "Create Assignment", true, false));
		Assignment assignments = new Assignment();

		if (id != null) {
			assignments = assignmentService.findByID(id);
			m.addAttribute("edit", "true");

		}

		studentassignment.setCourseId(courseId);
		studentassignment.setCourse(courseService.findByID(studentassignment.getCourseId()));
		m.addAttribute("studentassignment", assignments);
		return "assignment/updateStudentAssignmnent";
	}
	@Secured({ "ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/updateStudentAssignment", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateStudentAssignment(@ModelAttribute StudentAssignment assignment,
			@RequestParam("file") MultipartFile file, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment", "Create Assignment", true, false));
		String errorMessage = null;

		try {

			String username = principal.getName();
			Token userDetails = (Token) principal;

			m.addAttribute("allCourses", courseService.findByUserActive(username, userDetails.getProgramName()));

			assignment.setLastModifiedBy(username);
			studentAssignmentService.update(assignment);
			assignment.setCourse(courseService.findByID(assignment.getCourseId()));
			setSuccess(m, "Assigment updated successfully");

			List<StudentAssignment> students = studentAssignmentService.getStudentsForAssignment(assignment.getId(),
					assignment.getCourseId());
			m.addAttribute("students", students);
		}

		catch (Exception e) {
			logger.error("Exception", e);
			setError(m, "Error in updating assignment");
			m.addAttribute("webPage", new WebPage("assignment", "Create Assignment", false, false));
			return "assignment/createAssignment";
		}
		return "assignment/updatedAssignment";
	}
	@Secured({ "ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/evaluateByStudentForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String evaluateByStudentForm(Model m, Long id, @ModelAttribute StudentAssignment assignment,
			Principal principal, @RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignmentList", "Search Assignments", false, false));
		m.addAttribute("assignment", assignment);
		List<Assignment> assigments = new ArrayList<>();
		if (courseId == null || courseId.isEmpty()) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord").equals("")) {

			} else {
				request.getSession().removeAttribute("courseRecord");
			}
			m.addAttribute("preAssigments", assigments);
		} else {
			assigments = assignmentService.findByFacultyAndCourseActive_new(username,
					Long.valueOf(assignment.getCourseId()));
			m.addAttribute("preAssigments", assigments);
		}

		m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));
		m.addAttribute("preAssigment", new ArrayList());

		/*
		 * Assignment assignments = assignmentService.findByID(id); assignment.setCourse
		 * (courseService.findByID(assignments.getCourseId()));
		 */
		m.addAttribute("allAssignments", assignmentService.findAll());
		return "assignment/evaluateByStudent";
	}

//	@RequestMapping(value = "/sendFile", method = { RequestMethod.GET,
//			RequestMethod.POST })
//	public ModelAndView sendFile(@RequestParam(name = "id") String id,
//			HttpServletRequest request, HttpServletResponse response) {
//
//		String projectUrl = "";
//		try {
//			StudentAssignment sa = studentAssignmentService.findByID(Long
//					.valueOf(id));
//			File src = new File(sa.getStudentFilePath());
//			File dest = new File(workDir + File.separator + src.getName());
//
//			if (dest.exists())
//				dest.delete();
//			FileUtils.copyFile(src, dest);
//
//			projectUrl = "/" + "workDir" + "/" + dest.getName();
//
//		} catch (Exception e) {
//			logger.error("Error", e);
//		}
//
//		return new ModelAndView("redirect:" + projectUrl);
//	}

	@RequestMapping(value = "/sendFile", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView sendFile(@RequestParam(name = "id") String id, HttpServletRequest request,
			HttpServletResponse response) {

		String projectUrl = "";
		try {
			StudentAssignment sa = studentAssignmentService.findByID(Long.valueOf(id));

			String path = "";
			if (sa.getStudentFilePath().startsWith("/")) {
				path = StringUtils.substring(sa.getStudentFilePath(), 1);
			} else {
				path = sa.getStudentFilePath();
			}
			path = path.replace("/\\", "/");
			path = path.replace("\\\\", "/");
			path = path.replace("\\", "/");
			path = path.replace("//", "/");
			File src = new File(path);
			InputStream inpStream = amazonS3ClientService.getFileByFullPath(path);
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyInputStreamToFile(inpStream, dest);

			projectUrl = "/" + "workDir" + "/" + dest.getName();

		} catch (Exception e) {
			logger.error("Error", e);
		}

		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/sendFileForAssignment", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView sendFileForAssignment(@RequestParam(name = "id") String id, HttpServletRequest request,
			HttpServletResponse response) {

		String projectUrl = "";
		try {
			Assignment a = assignmentService.findByID(Long.valueOf(id));
			File src = new File(a.getFilePreviewPath());
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyFile(src, dest);

			projectUrl = "/" + "workDir" + "/" + dest.getName();

		} catch (Exception e) {
			logger.error("Error", e);
		}

		return new ModelAndView("redirect:" + projectUrl);
	}
	@Secured({ "ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/evaluateByStudent", method = { RequestMethod.GET, RequestMethod.POST })
	public String evaluateByStudent(@RequestParam(required = false, defaultValue = "1") int pageNo, Principal principal,
			Model m, @ModelAttribute Assignment assignment, Long id) {
		m.addAttribute("webPage",
				new WebPage("evaluateAssignment", "Search Assignment To Evaluate", true, false, true, true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		logger.info("AssignmentId---------->" + id);
		logger.info("AssignmentId---------->" + assignment.getId());
		m.addAttribute("assignmentId", id);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			StudentAssignment sa = new StudentAssignment();
			sa.setEvaluatedBy(username);

			// Page<StudentAssignment> page =
			// studentAssignmentService.findAssignmentForEvaluation(assignment,
			// pageNo, pageSize, username);
			List<StudentAssignment> assignmentsList = studentAssignmentService.getStudentsBasedOnAssignment(sa, id);
			logger.info("assignmentsList----->" + assignmentsList);
			logger.info("assignmentsList Size----->" + assignmentsList.size());
			// Boolean check = false;
			Boolean plagCheck = false;
//			for (StudentAssignment ss : assignmentsList) {
//				logger.info("sa.id-->"+ss.getId() +"ss.getStudentFilePath----->"+ss.getStudentFilePath());
//				if (ss.getStudentFilePath().endsWith(".pdf")) {
//
//					check = true;
//
//				} else {
//
//					check = false;
//					break;
//				}
//
//			}
			for (StudentAssignment ss : assignmentsList) {
				logger.info("Threshold--->" + ss.getThreshold());
				if (ss.getThreshold() != null) {
					plagCheck = true;
				} else {
					plagCheck = false;
					break;
				}
			}
			m.addAttribute("plagCheck", plagCheck);
//			if (check) {
//				m.addAttribute("checkCopy", true);
//
//			} else {
//				m.addAttribute("checkCopy", false);
//			}

			List<Assignment> assigments = new ArrayList();
			if (assignment.getCourseId() != null)
				assigments = assignmentService.findByFacultyAndCourseActive_new(username,
						Long.valueOf(assignment.getCourseId()));

			for (StudentAssignment uc : assignmentsList) {
				User u1 = userService.findByUserName(uc.getUsername());
				uc.setRollNo(u1.getRollNo());
				String studentFilePath = StringUtils.trimToNull(uc.getStudentFilePath());
				if (studentFilePath != null) {
					uc.setShowStudentFileDownload("true");
				} else {
					uc.setShowStudentFileDownload("false");
				}
				Assignment a = assignmentService.findByID(uc.getAssignmentId());
				assignment.setAssignmentName(a.getAssignmentName());
				String filePath = StringUtils.trimToNull(a.getFilePath());
				if (filePath != null) {
					uc.setShowFileDownload("true");
				} else {
					uc.setShowFileDownload("false");
				}
				assignmentsList.set(assignmentsList.indexOf(uc), uc);
			}

			m.addAttribute("assignmentsList", assignmentsList);
			m.addAttribute("q", getQueryString(assignment));
			m.addAttribute("assignment", assignment);
			m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));
			m.addAttribute("preAssigments", assigments);

			if (assignmentsList == null || assignmentsList.size() == 0) {
				setNote(m, "No Assignments found");
			}

			List<AssignmentConfiguration> ac = assignmentConfigurationService.findAllByAssignmentId(id);
			if(ac.size()>0) {
				m.addAttribute("showQuestionwiseEvaluate", true);
			}else {
				m.addAttribute("showQuestionwiseEvaluate", false);
			}
			/*
			 * List<StudentAssignment> students = studentAssignmentService
			 * .getStudentsBasedOnAssignment(id); m.addAttribute("students", students);
			 */

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Student List");
		}
		logger.info("AssignmentId obj---------->" + assignment.getId());
		return "assignment/evaluateByStudent";
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/lateSubmissionApprovalForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String lateSubmissionApprovalForm(Model m, Long id, @ModelAttribute StudentAssignment assignment,
			Principal principal, @RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("assignmentList", "Search Assignments", false, false));
		m.addAttribute("assignment", assignment);
		m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));

		if (courseId == null || courseId.isEmpty()) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord").equals("")) {

			} else {
				request.getSession().removeAttribute("courseRecord");
			}

		} else {
			List<Assignment> assigments = assignmentService.findByFacultyAndCourseActive(username,
					Long.valueOf(assignment.getCourseId()));
			m.addAttribute("preAssigments", assigments);
		}
		/*
		 * Assignment assignments = assignmentService.findByID(id); assignment.setCourse
		 * (courseService.findByID(assignments.getCourseId()));
		 */
		m.addAttribute("allAssignments", assignmentService.findAll());
		return "assignment/lateSubmissionStudents";
	}
	@Secured({ "ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/lateSubmissionApproval", method = { RequestMethod.GET, RequestMethod.POST })
	public String lateSubmissionApproval(@RequestParam(required = false, defaultValue = "1") int pageNo,
			Principal principal, Model m, @ModelAttribute Assignment assignment, Long id) {
		m.addAttribute("webPage",
				new WebPage("LateSubmissionApproval", "Select Assignments To Approve", true, false, true, true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			StudentAssignment sa = new StudentAssignment();
			sa.setEvaluatedBy(username);

			// Page<StudentAssignment> page =
			// studentAssignmentService.findAssignmentForEvaluation(assignment,
			// pageNo, pageSize, username);
			List<StudentAssignment> assignmentsList = studentAssignmentService.getLateSubmittedStudents(sa, id);

			List<Assignment> assigments = new ArrayList();
			if (assignment.getCourseId() != null)
				assigments = assignmentService.findByFacultyAndCourseActive(username,
						Long.valueOf(assignment.getCourseId()));

			for (StudentAssignment uc : assignmentsList) {
				User u1 = userService.findByUserName(uc.getUsername());
				uc.setRollNo(u1.getRollNo());
				assignmentsList.set(assignmentsList.indexOf(uc), uc);
			}

			m.addAttribute("assignmentsList", assignmentsList);
			m.addAttribute("q", getQueryString(assignment));
			m.addAttribute("assignment", assignment);
			m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));
			m.addAttribute("preAssigments", assigments);

			if (assignmentsList == null || assignmentsList.size() == 0) {
				setNote(m, "No Assignments found");
			}

			/*
			 * List<StudentAssignment> students = studentAssignmentService
			 * .getStudentsBasedOnAssignment(id); m.addAttribute("students", students);
			 */

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Student List");
		}

		return "assignment/lateSubmissionStudents";
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/nonSubmittedStudentsForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String nonSubmittedStudentsForm(Model m, Long id, @ModelAttribute StudentAssignment assignment,
			Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("assignmentList", "Search Assignments", false, false));
		m.addAttribute("assignment", assignment);
		m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));
		m.addAttribute("preAssigment", new ArrayList());

		/*
		 * Assignment assignments = assignmentService.findByID(id); assignment.setCourse
		 * (courseService.findByID(assignments.getCourseId()));
		 */
		m.addAttribute("allAssignments", assignmentService.findAll());
		return "assignment/nonSubmittedStudents";
	}
	@Secured({ "ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/nonSubmittedStudents", method = { RequestMethod.GET, RequestMethod.POST })
	public String nonSubmittedStudents(@RequestParam(required = false, defaultValue = "1") int pageNo,
			Principal principal, Model m, @ModelAttribute Assignment assignment, Long id) {
		m.addAttribute("webPage",
				new WebPage("lateSubmissionApproval", "Select Assignments To Approve", true, false, true, true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			StudentAssignment sa = new StudentAssignment();
			sa.setEvaluatedBy(username);

			// Page<StudentAssignment> page =
			// studentAssignmentService.findAssignmentForEvaluation(assignment,
			// pageNo, pageSize, username);
			List<StudentAssignment> assignmentsList = studentAssignmentService.getNonSubmittedStudents(sa, id);

			List<Assignment> assigments = new ArrayList();
			if (assignment.getCourseId() != null)
				assigments = assignmentService.findByFacultyAndCourseActive(username,
						Long.valueOf(assignment.getCourseId()));

			m.addAttribute("assignmentsList", assignmentsList);
			m.addAttribute("q", getQueryString(assignment));
			m.addAttribute("assignment", assignment);
			m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));
			m.addAttribute("preAssigments", assigments);

			if (assignmentsList == null || assignmentsList.size() == 0) {
				setNote(m, "No Students found");
			}

			/*
			 * List<StudentAssignment> students = studentAssignmentService
			 * .getStudentsBasedOnAssignment(id); m.addAttribute("students", students);
			 */

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Student List");
		}

		return "assignment/nonSubmittedStudents";
	}

	/*
	 * @RequestMapping(value = "/sendReminderToAllStudents", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * sendReminderToAllStudents(
	 * 
	 * @ModelAttribute StudentMessage st_message,
	 * 
	 * @ModelAttribute StudentAssignment assignment,
	 * 
	 * @RequestParam(required = false) String st_username,
	 * 
	 * @RequestParam(required = false) String assgn_name,
	 * 
	 * @RequestParam Long courseId, Model m, Principal principal) {
	 * m.addAttribute("webPage", new WebPage("message", "Message Details", true,
	 * true)); String username = principal.getName();
	 * UsernamePasswordAuthenticationToken userDeatils =
	 * (UsernamePasswordAuthenticationToken) principal; try {
	 * 
	 * StudentAssignment sa = new StudentAssignment(); List<StudentAssignment>
	 * studentMessageList = studentAssignmentService
	 * .findAssignmentForEvaluation(assignment, username);
	 * logger.info(" studentMessageList ---------" + studentMessageList);
	 * List<StudentMessage> studentMessageMappingList = new
	 * ArrayList<StudentMessage>(); List<String> sm = new ArrayList<String>(); for
	 * (StudentAssignment s : studentMessageList) { sm.add(s.getUsername());
	 * 
	 * }
	 * 
	 * for (int i = 1; i <= sm.size(); i++) {
	 * 
	 * st_message.setUsername(st_username); st_message.setCreatedBy(username);
	 * st_message.setLastModifiedBy(username);
	 * st_message.setSubject("Assignment 'SUBMISSION' Reminder !"); st_message
	 * .setDescription("Kindly Submit Your Assignment  " + assgn_name +
	 * " before End Date or else your marks will be too LOW !");
	 * studentMessageMappingList.add(st_message); }
	 * logger.info("studentMessageMappingList--------" + studentMessageMappingList);
	 * studentMessageService.insertBatch(studentMessageMappingList);
	 * studentMessageService.insertWithIdReturn(st_message);
	 * m.addAttribute("webPage", new WebPage("assignmentList", "Search Assignments",
	 * false, false)); m.addAttribute("assignment", assignment);
	 * m.addAttribute("allCourses", courseService.findByUserActive(username));
	 * m.addAttribute("preAssigment", new ArrayList());
	 * m.addAttribute("allAssignments", assignmentService.findAll()); setSuccess(m,
	 * "Reminder Sent successfully !! ");
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e); setError(m,
	 * "Error in Sending Reminder"); return "Error"; } return
	 * "assignment/reminderSent"; }
	 */

	@RequestMapping(value = "/sendReminderToAllStudents", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendReminderToAllStudents(@ModelAttribute StudentMessage st_message,
			@ModelAttribute StudentAssignment assignment, @RequestParam(required = false) String st_username,
			@RequestParam(required = false) String assgn_name, @RequestParam Long courseId,
			@RequestParam(required = false) String submissionStatus, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("message", "Message Details", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<String> assignmentNameList = new ArrayList<String>();
		UsernamePasswordAuthenticationToken userDeatils = (UsernamePasswordAuthenticationToken) principal;
		try {

			List<String> studentList = new ArrayList<String>();
			List<StudentAssignment> studentMessageList = studentAssignmentService
					.findAssignmentEvaluationForNonSunmittedStudents(username);

			List<StudentMessage> studentMessageMappingList = new ArrayList<StudentMessage>();
			for (StudentAssignment sa : studentMessageList) {
				studentList.add(sa.getUsername());

				assignmentNameList.add(sa.getAssignmentName());

			}

			for (int i = 1; i <= studentMessageList.size(); i++) {
				StudentMessage message = new StudentMessage();
				message.setUsername(studentList.get(i - 1));

				message.setCreatedBy(username);
				message.setLastModifiedBy(username);
				message.setSubject("Assignment 'SUBMISSION' Reminder !");
				// message.setDescription("Kindly Submit Your Assignment null before End Date or
				// else your marks will be too LOW !");
				message.setDescription("Kindly Submit Your Assignment " + assignmentNameList.get(i - 1)
						+ " before End Date or else your marks will be too LOW !");
				studentMessageMappingList.add(message);

			}

			studentMessageService.insertBatch(studentMessageMappingList);

			m.addAttribute("webPage", new WebPage("assignmentList", "Search Assignments", false, false));
			m.addAttribute("assignment", assignment);
			m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));
			m.addAttribute("preAssigment", new ArrayList());
			m.addAttribute("allAssignments", assignmentService.findAll());
			setSuccess(m, "Reminder Sent successfully !! ");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in Sending Reminder");
			return "Error";
		}
		return "assignment/reminderSent";
	}

	@RequestMapping(value = "/sendReminder", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendReminder(@ModelAttribute StudentMessage st_message, @ModelAttribute StudentAssignment assignment,
			@RequestParam String st_username, @RequestParam String assgn_name, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("message", "Message Details", true, false));

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		UsernamePasswordAuthenticationToken userDeatils = (UsernamePasswordAuthenticationToken) principal;
		try {

			StudentAssignment sa = new StudentAssignment();
			sa.setEvaluatedBy(username);

			st_message.setUsername(st_username);
			st_message.setCreatedBy(username);
			st_message.setLastModifiedBy(username);
			st_message.setSubject("Assignment 'SUBMISSION' Reminder !");
			st_message.setDescription("Kindly Submit Your Assignment  " + assgn_name
					+ " before End Date or else your marks will be too LOW !");

			// st_message.setFacultyId(username);

			studentMessageService.insertWithIdReturn(st_message);

			m.addAttribute("webPage", new WebPage("assignmentList", "Search Assignments", false, false));
			m.addAttribute("assignment", assignment);
			m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));
			m.addAttribute("preAssigment", new ArrayList());

			/*
			 * Assignment assignments = assignmentService.findByID(id); assignment.setCourse
			 * (courseService.findByID(assignments.getCourseId()));
			 */
			m.addAttribute("allAssignments", assignmentService.findAll());

			setSuccess(m, "Reminder Sent successfully !! ");

			// m.addAttribute("student_message", st_message);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in Sending Reminder");

			return "Error";
		}
		// m.addAttribute("message", message);

		return "assignment/reminderSent";
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/getAssigmentByCourse", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAssigmentByCourse(@RequestParam(name = "courseId") String courseId,
			Principal principal, @ModelAttribute Assignment assignment) {
		String json = "";
		String username = principal.getName();
		List<Assignment> assigments = new ArrayList<Assignment>();
		if (assignment.getCourseId() != null) {
			assigments = assignmentService.findByFacultyAndCourseActive_new(username,
					Long.valueOf(assignment.getCourseId()));
		}
		/*
		 * List<Assignment> assigments = assignmentService
		 * .findByFacultyAndCourseActive(username, Long.valueOf(courseId));
		 */
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

		// logger.info("json" + json);
		return json;

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/getAssigmentByCourseForLateSubmitted", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAssigmentByCourseForLateSubmitted(@RequestParam(name = "courseId") String courseId,
			Principal principal, @ModelAttribute Assignment assignment) {
		String json = "";
		String username = principal.getName();
		List<Assignment> assigments = new ArrayList<Assignment>();
		if (assignment.getCourseId() != null) {
			assigments = assignmentService.findByFacultyAndCourseActive(username,
					Long.valueOf(assignment.getCourseId()));
		}
		/*
		 * List<Assignment> assigments = assignmentService
		 * .findByFacultyAndCourseActive(username, Long.valueOf(courseId));
		 */
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

		// logger.info("json" + json);
		return json;

	}
	/*
	 * @RequestMapping(value = "/evaluateByStudentFormGroup", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * evaluateByStudentFormGroup(Model m, Long id,
	 * 
	 * @ModelAttribute Assignment assignment) { m.addAttribute("webPage", new
	 * WebPage("assignmentList", "Search Assignments", false, false));
	 * m.addAttribute("assignment", assignment); m.addAttribute("allCourses",
	 * courseService.findAll());
	 * 
	 * m.addAttribute("allGroups", groupService.findAll()); return
	 * "assignment/evaluateByStudentGroup"; }
	 * 
	 * @RequestMapping(value = "/evaluateByStudentGroup", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * evaluateByStudentGroup( Principal principal, Model m, @ModelAttribute
	 * Assignment assignment, Long id) {
	 * 
	 * m.addAttribute("webPage", new WebPage("evaluateAssignment",
	 * "Search Assignment To Evaluate", true, false, true, true, false)); String
	 * username = principal.getName(); Page<Assignment> studentAssignmentList =
	 * null; logger.info("Group Id"+assignment.getGroupId()); studentAssignmentList
	 * = assignmentService.getAssignmentBasedOnGroups( assignment, 1, max_page_size,
	 * Long.valueOf(assignment.getGroupId()));
	 * 
	 * m.addAttribute("studentAssignmentList",
	 * studentAssignmentList.getPageItems());
	 * m.addAttribute("rowCount",studentAssignmentList.getRowCount());
	 * m.addAttribute("assignment", assignment); m.addAttribute("allCourses",
	 * courseService.findAll());
	 * 
	 * m.addAttribute("allGroups", groupService.findAll());
	 * 
	 * return "assignment/evaluateByStudentGroup"; }
	 */
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/evaluateByStudentFormGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public String evaluateByStudentFormGroup(Model m, Long id, @ModelAttribute StudentAssignment studentassignment,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("evaluateAssignment", "Search Assignments", false, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("studentassignment", studentassignment);
		m.addAttribute("allCourses", courseService.findAll());

		m.addAttribute("allGroups", groupService.findAll());
		return "assignment/evaluateByStudentGroup";
	}

	/*
	 * @RequestMapping(value = "/evaluateByStudentGroup", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * evaluateByStudentGroup(Principal principal, Model m,
	 * 
	 * @ModelAttribute StudentAssignment studentassignment, Long id) {
	 * 
	 * m.addAttribute("webPage", new WebPage("evaluateAssignment",
	 * "Search Assignment To Evaluate", true, false, true, true, false)); String
	 * username = principal.getName();
	 * 
	 * List<StudentAssignment> studentAssignmentList = new
	 * ArrayList<StudentAssignment>(); logger.info("Group Id" +
	 * studentassignment.getGroupId()); if (studentassignment != null)
	 * studentAssignmentList = studentAssignmentService
	 * .getAssignmentBasedOnGroups(studentassignment,
	 * 
	 * Long.valueOf(studentassignment.getGroupId()));
	 * 
	 * m.addAttribute("studentAssignmentList", studentAssignmentList);
	 * m.addAttribute("rowCount", studentAssignmentList.size());
	 * m.addAttribute("studentassignment", studentassignment);
	 * m.addAttribute("allCourses", courseService.findByUserActive(username));
	 * m.addAttribute("allGroups", groupService.findAll());
	 * 
	 * return "assignment/evaluateByStudentGroup"; }
	 */
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/getGroupByCourse", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getGroupByCourse(@RequestParam(name = "courseId") String courseId,
			Principal principal) {
		String json = "";
		String username = principal.getName();

		List<Groups> groups = groupService.findByFacultyAndCourseActiveWithNonEmptyGroups(username,
				Long.valueOf(courseId));
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Groups grp : groups) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(String.valueOf(grp.getId()), grp.getGroupName());
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
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/saveAssignmentScore", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveAssignmentScore(@RequestParam String value, @RequestParam Long pk,
			Principal principal) {
		String username = principal.getName();
		try {
			StudentAssignment submission = studentAssignmentService.findByID(pk);
			Assignment assignment = assignmentService.findByID(submission.getAssignmentId());

			// Comment below line and convert to Double if you want to allow
			// decimal scores for any institute
			String score = value;

			String maxScore = assignment.getMaxScore();
			if (maxScore != null && Integer.parseInt(score) > Integer.parseInt(maxScore)) {
				return "{\"status\": \"error\", \"msg\": \"Score cannot be greater than " + maxScore + "\"}";
			} else if (Integer.parseInt(score) < 0) {
				return "{\"status\": \"error\", \"msg\": \"Score cannot negative.\"}";
			}
			studentAssignmentService.saveAssignmentScore(score, pk, username);
			return "{\"status\": \"success\", \"msg\": \"Score saved successfully!\"}";

		} catch (NumberFormatException e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Please enter valid whole number!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Score!\"}";
		}

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/saveAssignmentRemarks", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveAssignmentRemarks(@RequestParam String value, @RequestParam Long pk,
			Principal principal) {
		String username = principal.getName();
		try {
			HtmlValidation.checkHtmlCode(value);
			HtmlValidation.checkHtmlCode(String.valueOf(pk));
			
			BusinessBypassRule.validateAlphaNumeric(value);
			studentAssignmentService.saveAssignmentRemarks(value, pk, username);
			return "{\"status\": \"success\", \"msg\": \"Remarks saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Remarks!\"}";
		}

	}
	public void validateLowScoreReason(String s) throws ValidationException{
		if (s == null || s.trim().isEmpty()) {
			 throw new ValidationException("Input field cannot be empty");
		 }
		if(!s.equals("Copy Case-Internet") && !s.equals("Copy Case-Other Student") && !s.equals("Wrong Answers") &&
				!s.equals("Other subject Assignment") && !s.equals("Scanned/Handwritten Assignment") && !s.equals("Only Questions written") && 
				!s.equals("Question Paper Uploaded") && !s.equals("Blank Assignment") && !s.equals("Corrupt file uploaded")) {
			throw new ValidationException("Invalid Reason.");
		}
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/saveLowScoreReason", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveLowScoreReason(@RequestParam String value, @RequestParam Long pk,
			Principal principal) {
		String username = principal.getName();
		try {
			validateLowScoreReason(value);
			studentAssignmentService.saveLowScoreReason(value, pk, username);
			return "{\"status\": \"success\", \"msg\": \"Reason saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Reason!\"}";
		}

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/saveApprovalStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveApprovalStatus(@ModelAttribute StudentMessage st_message,
			@RequestParam String value, @RequestParam Long pk, Principal principal) {

		String username = principal.getName();
		try {
			studentAssignmentService.saveApprovalStatus(value, pk, username);
			StudentAssignment sa = studentAssignmentService.findByID(pk);
			StudentAssignment sa1 = studentAssignmentService.findByID(sa.getAssignmentId());

			/*
			 * studentAssignmentService.getAssignId(assignment.getAssignmentId()) ;
			 */

			Long asignmentId = sa.getAssignmentId();

			Assignment assignment1 = assignmentService.findByID(asignmentId);

			if (sa.getApprovalStatus().equalsIgnoreCase("Approve")) {

				sa.setSubmissionStatus("Y");
				studentAssignmentService.update(sa);
			} else {
				sa.setSubmissionStatus("N");
			}

			if (sa.getApprovalStatus().equalsIgnoreCase("Reject")) {
				st_message.setUsername(sa.getUsername());
				st_message.setCreatedBy(username);
				st_message.setLastModifiedBy(username);
				st_message.setSubject("Assignment REJECTION !");
				st_message.setDescription(
						"Your Assignment '" + assignment1.getAssignmentName() + "' Has Been Rejected !");
				studentMessageService.insertWithIdReturn(st_message);
			}
			return "{\"status\": \"success\", \"msg\": \"Status saved successfully!\"}";

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Status!\"}";
		}

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/viewAssignmentSubmission", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewAssignmentSubmission(@ModelAttribute StudentAssignment assignmentSubmission, Model m,
			@RequestParam String username, Principal principal) {
		// String username = principal.getName();
		m.addAttribute("webPage",
				new WebPage("viewAssignmentSubmission", "Student Submission", false, false, true, true, false));

		Assignment assignment = assignmentService.findByID(assignmentSubmission.getAssignmentId());
		assignmentSubmission = studentAssignmentService.findAssignmentSubmission(username, assignment.getId());
		assignment.setCourse(courseService.findByID(assignment.getCourseId()));
		m.addAttribute("assignment", assignment);
		m.addAttribute("assignmentSubmission", assignmentSubmission);
		return "assignment/submitAssignment";
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/saveScore", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveScore(@RequestParam String value, @RequestParam Long pk, Principal principal) {
		String username = principal.getName();
		try {
			studentAssignmentService.saveLowScoreReason(value, pk, username);
			return "{\"status\": \"success\", \"msg\": \"Reason saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Reason!\"}";
		}

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/assignmentDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public String assignmentDetails(@RequestParam(required = true) long id, Model m, Principal principal) {
		String username = principal.getName();
		m.addAttribute("webPage",
				new WebPage("assignmentDetails", "Assignment Detail", false, false, true, true, false));

		Assignment assignment = assignmentService.findByID(id);
		assignment.setCourse(courseService.findByID(assignment.getCourseId()));

		StudentAssignment assignmentSubmission = studentAssignmentService.findAssignmentSubmission(username, id);

		m.addAttribute("assignmentSubmission", assignmentSubmission);
		m.addAttribute("assignment", assignment);

		/*
		 * m.addAttribute("announcmentList", dashBoardService
		 * .listOfAnnouncementsForCourseList(username, null));
		 * 
		 * m.addAttribute("toDoList", dashBoardService.getToDoList(username));
		 */
		return "assignment/assignmentDetailsNew";
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/evaluateByStudentGroupForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String evaluateByStudentGroupForm(Model m, Long id, @ModelAttribute StudentAssignment assignment,
			Principal principal, @RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {
		Token userDetails = (Token) principal;
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);

		String acadSession = u1.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignmentList", "Search Assignments", false, false));
		m.addAttribute("assignment", assignment);
		List<Assignment> assigments = new ArrayList<>();
		if (courseId == null || courseId.isEmpty()) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord").equals("")) {

			} else {
				request.getSession().removeAttribute("courseRecord");
			}

		}else {
			assigments = assignmentService.findByFacultyAndCourseActive_new(username,
					Long.valueOf(assignment.getCourseId()));
		}

		m.addAttribute("allCourses", courseService.findByUserActive(username, userDetails.getProgramName()));
		m.addAttribute("preAssigments", assigments);

		/*
		 * Assignment assignments = assignmentService.findByID(id); assignment.setCourse
		 * (courseService.findByID(assignments.getCourseId()));
		 */
		m.addAttribute("allAssignments", assignmentService.findAll());
		return "assignment/evaluateByStudentGroup";
	}

	/*
	 * @RequestMapping(value = "/evaluateByStudentGroup", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * evaluateByStudentGroup(
	 * 
	 * @RequestParam(required = false, defaultValue = "1") int pageNo, Principal
	 * principal, Model m,
	 * 
	 * @ModelAttribute Assignment assignment, Long id) { m.addAttribute("webPage",
	 * new WebPage("evaluateAssignment", "Search Assignment To Evaluate", true,
	 * false, true, true, false)); String username = principal.getName();
	 * 
	 * try { StudentAssignment sa = new StudentAssignment();
	 * sa.setEvaluatedBy(username);
	 * 
	 * 
	 * List<StudentAssignment> groupList =
	 * studentAssignmentService.getGroupsBasedOnCourseAndAssignment
	 * (assignment.getCourseId(), id); List<StudentAssignment> assignmentsList =
	 * studentAssignmentService .getStudentsBasedOnAssignment(sa, id);
	 * logger.info("groupList" + groupList); List<Assignment> assigments = new
	 * ArrayList(); if (assignment.getCourseId() != null) assigments =
	 * assignmentService.findByFacultyAndCourseActive( username,
	 * Long.valueOf(assignment.getCourseId()));
	 * 
	 * m.addAttribute("groupList", groupList); m.addAttribute("q",
	 * getQueryString(assignment)); m.addAttribute("assignment", assignment);
	 * m.addAttribute("allCourses", courseService.findByUserActive(username));
	 * m.addAttribute("preAssigments", assigments);
	 * 
	 * if (groupList == null || groupList.size() == 0) { setError(m,
	 * "No Assignments found"); }
	 * 
	 * 
	 * List<StudentAssignment> students = studentAssignmentService
	 * .getStudentsBasedOnAssignment(id); m.addAttribute("students", students);
	 * 
	 * 
	 * } catch (Exception e) {
	 * 
	 * logger.error("Exception", e); setError(m, "Error in getting Student List"); }
	 * 
	 * return "assignment/evaluateByStudentGroup"; }
	 */
	/*
	 * @RequestMapping(value = "/evaluateByStudentGroup", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * evaluateByStudentGroup(
	 * 
	 * @RequestParam(required = false, defaultValue = "1") int pageNo, Principal
	 * principal, Model m,
	 * 
	 * @ModelAttribute Assignment assignment, Long id) { m.addAttribute("webPage",
	 * new WebPage("evaluateAssignment", "Search Assignment To Evaluate", true,
	 * false, true, true, false)); String username = principal.getName(); Long
	 * studentAssignmentId = null;
	 * 
	 * try { StudentAssignment sa = new StudentAssignment();
	 * sa.setEvaluatedBy(username);
	 * 
	 * List<StudentAssignment> groupList = studentAssignmentService
	 * .getGroupsBasedOnCourseAndAssignment( assignment.getCourseId(), id);
	 * List<StudentAssignment> assignmentsList = studentAssignmentService
	 * .getStudentsBasedOnAssignment(sa, id); for (StudentAssignment student :
	 * assignmentsList) { studentAssignmentId = student.getId();
	 * logger.info("Student Assignment Id is---------------" + studentAssignmentId);
	 * 
	 * break;
	 * 
	 * } for (StudentAssignment student : groupList) {
	 * 
	 * logger.info("Student Assignment id is -----------" + studentAssignmentId);
	 * student.setStudentId(String.valueOf(studentAssignmentId));
	 * 
	 * groupList.set(groupList.indexOf(student), student); break; }
	 * 
	 * logger.info("groupList" + groupList); List<Assignment> assigments = new
	 * ArrayList(); if (assignment.getCourseId() != null) assigments =
	 * assignmentService.findByFacultyAndCourseActive( username,
	 * Long.valueOf(assignment.getCourseId()));
	 * 
	 * m.addAttribute("groupList", groupList); m.addAttribute("q",
	 * getQueryString(assignment)); m.addAttribute("assignment", assignment);
	 * m.addAttribute("allCourses", courseService.findByUserActive(username));
	 * m.addAttribute("preAssigments", assigments);
	 * 
	 * if (groupList == null || groupList.size() == 0) { setError(m,
	 * "No Assignments found"); }
	 * 
	 * 
	 * List<StudentAssignment> students = studentAssignmentService
	 * .getStudentsBasedOnAssignment(id); m.addAttribute("students", students);
	 * 
	 * 
	 * } catch (Exception e) {
	 * 
	 * logger.error("Exception", e); setError(m, "Error in getting Student List"); }
	 * 
	 * return "assignment/evaluateByStudentGroup"; }
	 */
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/evaluateByStudentGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public String evaluateByStudentGroup(@RequestParam(required = false, defaultValue = "1") int pageNo,
			Principal principal, Model m, @RequestParam(required = false) String submissionStatus,
			@ModelAttribute Assignment assignment, Long id) {
		m.addAttribute("webPage",
				new WebPage("evaluateAssignment", "Search Assignment To Evaluate", true, false, true, true, false));
		String username = principal.getName();
		Token userDetails = (Token) principal;

		Long studentAssignmentId = null;
		Integer count = null;
		List<StudentAssignment> groupList = new ArrayList<StudentAssignment>();

		try {
			StudentAssignment sa = new StudentAssignment();
			sa.setEvaluatedBy(username);
			sa.setSubmissionStatus(submissionStatus);
			sa.setCourseId(assignment.getCourseId());
			sa.setAssignmentId(id);
			/*
			 * List<StudentAssignment> groupListFromDB = studentAssignmentService
			 * .getGroupsBasedOnCourseAndAssignment(sa, assignment.getCourseId(), id);
			 */

			List<StudentAssignment> groupListFromDB = studentAssignmentService
					.getGroupsBasedOnCourseAndAssignmentWithGroupCourse(sa, assignment.getCourseId(), id);
			// logger.info("Group List size before is "+groupListFromDB.size());
			if (submissionStatus.equals("Y")) {
				// for(int i=1;i<=groupList.size();i++){
				for (StudentAssignment st : groupListFromDB) {

					count = Integer.valueOf(
							studentAssignmentService.findByGroupAndAssignmentId(st.getGroupId(), st.getAssignmentId()));

					if (count != 2) {
						// groupList.remove(groupList.indexOf(st));
						groupList.add(st);

					} else {

					}

				}
			}
			// }
			else {
				for (StudentAssignment st : groupListFromDB) {

					count = Integer.valueOf(
							studentAssignmentService.findByGroupAndAssignmentId(st.getGroupId(), st.getAssignmentId()));
					logger.info("Count is " + count);

					if (count == 1) {

						if (st.getSubmissionStatus().equals("Y")) {

							st.setSubmissionStatus("Y");
							groupListFromDB.set(groupListFromDB.indexOf(st), st);
						} else {

							st.setSubmissionStatus("N");
							groupListFromDB.set(groupListFromDB.indexOf(st), st);
						}
					} else {

						st.setSubmissionStatus("N");
						groupListFromDB.set(groupListFromDB.indexOf(st), st);

					}
					// groupList.add(st);

				}

				groupList.addAll(groupListFromDB);
			}
			List<StudentAssignment> assignmentsList = studentAssignmentService.getStudentsBasedOnAssignment(sa, id);
			for (StudentAssignment student : assignmentsList) {
				studentAssignmentId = student.getId();

				break;

			}
			for (StudentAssignment student : groupList) {

				student.setStudentId(String.valueOf(studentAssignmentId));

				groupList.set(groupList.indexOf(student), student);
				break;
			}

			List<Assignment> assigments = new ArrayList();
			if (assignment.getCourseId() != null)
				assigments = assignmentService.findByFacultyAndCourseActive(username,
						Long.valueOf(assignment.getCourseId()));

			m.addAttribute("groupList", groupList);
			m.addAttribute("q", getQueryString(assignment));
			m.addAttribute("assignment", assignment);
			m.addAttribute("allCourses", courseService.findByUserActive(username, userDetails.getProgramName()));
			m.addAttribute("preAssigments", assigments);

			if (groupList == null || groupList.size() == 0) {
				setNote(m, "No Assignments found");
			}

			/*
			 * List<StudentAssignment> students = studentAssignmentService
			 * .getStudentsBasedOnAssignment(id); m.addAttribute("students", students);
			 */

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Student List");
		}

		return "assignment/evaluateByStudentGroup";
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/saveGroupAssignmentScore", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveGroupAssignmentScore(@RequestParam String value, @RequestParam Long groupId,
			@RequestParam Long pk, Principal principal) {
		String username = principal.getName();
		try {

			/*
			 * StudentAssignment submission = studentAssignmentService .findByID(pk);
			 */
			Assignment assignment = assignmentService.findByID(pk);
			// StudentAssignment submission =
			// studentAssignmentService.findAssignmentSubmission(username, pk);

			// Comment below line and convert to Double if you want to allow
			// decimal scores for any institute
			String score = value;

			String maxScore = assignment.getMaxScore();
			if (maxScore != null && Integer.parseInt(score) > Integer.parseInt(maxScore)) {
				return "{\"status\": \"error\", \"msg\": \"Score cannot be greater than " + maxScore + "\"}";
			} else if (Integer.parseInt(score) < 0) {
				return "{\"status\": \"error\", \"msg\": \"Score cannot negative.\"}";
			}
			studentAssignmentService.saveGroupScore(score, groupId, username);
			return "{\"status\": \"success\", \"msg\": \"Score saved successfully!\"}";

		} catch (NumberFormatException e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Please enter valid whole number!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Score!\"}";
		}

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/saveGroupAssignmentRemarks", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveGroupAssignmentRemarks(@RequestParam String value, @RequestParam Long pk,
			Principal principal) {
		String username = principal.getName();
		try {
			studentAssignmentService.saveGroupAssignmentRemarks(value, username, pk);

			return "{\"status\": \"success\", \"msg\": \"Remarks saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Remarks!\"}";
		}

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/saveGroupLowScoreReason", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveGroupLowScoreReason(@RequestParam String value, @RequestParam Long pk,
			Principal principal) {
		String username = principal.getName();
		try {

			studentAssignmentService.saveGroupLowScoreReason(value, pk, username);
			return "{\"status\": \"success\", \"msg\": \"Reason saved successfully!\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\": \"error\", \"msg\": \"Error in saving Reason!\"}";
		}

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/checkCopiedAssignment", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView checkCopiedAssignment(Principal principal, Model m, @RequestParam Long assignmentId,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		String filePath = "";
		List<StudentAssignment> studentAssignmentFilesList = studentAssignmentService.getStduentFiles(assignmentId);
		Assignment assignment = assignmentService.findByID(assignmentId);
		Course c = courseService.findByID(assignment.getCourseId());
		StudentAssignment searchBean = new StudentAssignment();
		if (assignment.getThreshold() != null) {
			searchBean.setMinMatchPercent(assignment.getThreshold());
		} else {
			searchBean.setMinMatchPercent(50);
		}
		searchBean.setCourseName(c.getCourseName());
		searchBean.setAssignmentName(assignment.getAssignmentName());

		/*
		 * StudentAssignment questionFileBean = new StudentAssignment();
		 * questionFileBean.setFilePath(assignment.getFilePath());
		 */
		String projectUrl = "";
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			StudentAssignment questionFileBean = new StudentAssignment();
			if (assignment.getFilePath() != null) {
				questionFileBean.setFilePath(assignment.getFilePath());
			} else {
				String fileName = assignment.getAssignmentName() + " questionfile.pdf";
				String qFilePath = downloadAllFolder + File.separator + fileName;
				File folderPath = new File(downloadAllFolder);
				if (!folderPath.exists()) {
					folderPath.mkdirs();
				}
				PdfWriter writer = null;
				Document document = new Document();
				writer = PdfWriter.getInstance(document, new FileOutputStream(qFilePath));

				document.open();
				Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
				Chunk chunk = new Chunk(assignment.getAssignmentText(), font);

				document.add(chunk);
				document.close();

				File qFile = new File(qFilePath);
				amazonS3ClientService.uploadFile(qFile, assignmentFolder);
				String path = assignmentFolder + "/" + qFile.getName();
				questionFileBean.setFilePath(path);
				assignment.setFilePath(path);
				assignment.setFilePreviewPath(path);
				assignmentService.update(assignment);
				FileUtils.deleteQuietly(qFile);

			}
			File ip = new File(copyCaseHelper.checkCopyCases(studentAssignmentFilesList, searchBean, questionFileBean));
			filePath = ip.getAbsolutePath();
			if (StringUtils.isEmpty(filePath)) {
				request.setAttribute("error", "true");
				request.setAttribute("errorMessage", "Error in downloading file.");
			}

			// get absolute path of the application
			ServletContext context = request.getSession().getServletContext();
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
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// get output stream of the response
			outStream = response.getOutputStream();

			IOUtils.copy(inputStream, outStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m.addAttribute("assignment", assignment);

		return null;
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/supportAdminAssignmentList", method = { RequestMethod.GET, RequestMethod.POST })
	public String supportAdminAssignmentList(@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) String username, Model m, Principal principal) {
		/* String username = principal.getName(); */

		/*
		 * Token userdetails1 = (Token) principal; String ProgramName =
		 * userdetails1.getProgramName();
		 */User u = userService.findByUserName(username);

		/*
		 * Program programDetails = programService.findByID(u.getProgramId()); String
		 * ProgramName = programDetails.getProgramName(); String acadSession =
		 * u.getAcadSession(); logger.info("program details ---->" + programDetails);
		 * 
		 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
		 * acadSession);
		 */
		Course course = courseService.findByID(courseId);
		m.addAttribute("course", course);
		m.addAttribute("webPage", new WebPage("assignmentList", "Assignments", true, false));
		UserRole userDetails = userRoleService.findRoleByUsername(username);
		List<Assignment> assignments = Collections.emptyList();
		if (userDetails.getRole().equals(Role.ROLE_FACULTY)) {
			if (courseId != null) {
				assignments = assignmentService.findByFacultyAndCourseActive(username, courseId);
			}
		} else if (userDetails.getRole().equals(Role.ROLE_STUDENT)) {
			if (courseId != null) {
				assignments = assignmentService.findByUserAndCourse(username, courseId);
			}
		}

		if (assignments.size() == 0 || assignments.isEmpty()) {
			setNote(m, "No Assignments Found");
		}
		m.addAttribute("courseId", courseId);
		m.addAttribute("assignments", assignments);
		m.addAttribute("rowCount", assignments.size());
		m.addAttribute("username", username);
		/*
		 * m.addAttribute("announcmentList", dashBoardService
		 * .listOfAnnouncementsForCourseList(username, null));
		 * m.addAttribute("toDoList", dashBoardService.getToDoList(username));
		 */
		return "assignment/supportAdminAssignmentList";
		// return "assignment/assignmentListNew";
	}

	/*
	 * public Map<String, String> unzip(String zipFilePath, String destDir,
	 * List<String> listOfStudentsInAGroup, String assignmentNameWithId) {
	 * 
	 * // create output directory if it doesn't exist //File dir = new
	 * File(destDir); // create output directory if it doesn't exist
	 * 
	 * if (!dir.exists()) dir.mkdirs();
	 * 
	 * FileInputStream fis; // buffer for read and write data to file byte[] buffer
	 * = new byte[1024]; Map<String, String> fileMap = new HashMap<>(); try { fis =
	 * new FileInputStream(zipFilePath); ZipInputStream zis = new
	 * ZipInputStream(fis); ZipEntry ze = zis.getNextEntry(); while (ze != null) {
	 * String fileName = ze.getName();
	 * 
	 * File newFile = new File(downloadAllFolder + File.separator +
	 * assignmentNameWithId + File.separator + fileName); String fileNamewithoutExtn
	 * = FilenameUtils .removeExtension(newFile.getName());
	 * 
	 * 
	 * // create directories for sub directories in zip
	 * 
	 * // String error = //
	 * uploadAssignmentSubmissionFileFromZip(assignmentNameWithId,new //
	 * File(newFile.getParent())); // fileMap.put("ErrorInUploading", error); new
	 * File(newFile.getParent()).mkdirs(); FileOutputStream fos = new
	 * FileOutputStream(newFile); int len; while ((len = zis.read(buffer)) > 0) {
	 * fos.write(buffer, 0, len); } fos.close(); zis.closeEntry();
	 * 
	 * String filePath = destDir + File.separator + assignmentNameWithId +
	 * File.separator + fileName; amazonS3ClientService.uploadFile(newFile,
	 * filePath);
	 * 
	 * if (listOfStudentsInAGroup.contains(fileNamewithoutExtn)) {
	 * 
	 * fileMap.put(fileNamewithoutExtn, filePath); } else { fileMap.put("Error",
	 * fileNamewithoutExtn + "does not present in a group");
	 * 
	 * }
	 * 
	 * ze = zis.getNextEntry(); } // close last ZipEntry zis.closeEntry();
	 * zis.close(); fis.close(); } catch (IOException e) { e.printStackTrace(); }
	 * return fileMap; }
	 */
	
	
	public Map<String, String> unzip(String zipFilePath, String destDir,
			List<String> listOfStudentsInAGroup, String assignmentNameWithId) {

		// create output directory if it doesn't exist
		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		Map<String, String> fileMap = new HashMap<>();
		try {
			zipFilePath = zipFilePath.replace("\\", "/");
			System.out.println("zipFilePath-----> "+zipFilePath);
			
			fis = new FileInputStream(zipFilePath);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();

				System.out.println("FilePath_Doubt-----> "+destDir +"/"
						+ assignmentNameWithId + "/"+ fileName);
				
				File newFile = new File(destDir +"/"
						+ assignmentNameWithId + "/"+ fileName);
				String fileNamewithoutExtn = FilenameUtils
						.removeExtension(newFile.getName());

				if (listOfStudentsInAGroup.contains(fileNamewithoutExtn)) {

					fileMap.put(fileNamewithoutExtn, newFile.getPath());
				} else {
					fileMap.put("Error", fileNamewithoutExtn
							+ "does not present in a group");

				}
				
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zis.closeEntry();
				ze = zis.getNextEntry();
				System.out.println("unzip_aws_path-----> "+destDir +"/"+ assignmentNameWithId);
				amazonS3ClientService.uploadFile(newFile, destDir +"/"+ assignmentNameWithId);
			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileMap;
	}

	public File convert(MultipartFile file) {
		File convFile = new File(submissionFolder + "/" + file.getOriginalFilename());
		try {

			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
			System.out.println("convert_aws_path-----> " + submissionFolder);
			if (submissionFolder.endsWith("/")) {
				submissionFolder = submissionFolder.substring(0, submissionFolder.length() - 1);
			}
			amazonS3ClientService.uploadFile(convFile, submissionFolder);

		} catch (Exception ex) {

			logger.error("Exception ex");
		}
		return convFile;
	}

	/*
	 * public File convert(MultipartFile file) { File convFile = new
	 * File(downloadAllFolder + File.separator + file.getOriginalFilename()); try {
	 * 
	 * convFile.createNewFile(); FileOutputStream fos = new
	 * FileOutputStream(convFile); fos.write(file.getBytes()); fos.close();
	 * 
	 * } catch (Exception ex) {
	 * 
	 * logger.error("Exception ex"); } return convFile; }
	 */
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/submitAssignmentByOneInGroup", method = {

			RequestMethod.GET, RequestMethod.POST })
	public String submitAssignmentByOneInGroup(

			@RequestParam("id") Long assignmentId, @ModelAttribute StudentAssignment assignmentSubmission,

			@RequestParam("file") MultipartFile file, Model m, Principal principal, RedirectAttributes redirectAttrs) {

		m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment",

				false, false, true, true, false));
		Assignment assignment = assignmentService.findByID(assignmentId);
		String username = principal.getName();

		assignmentSubmission = studentAssignmentService.findAssignmentSubmission(username, assignmentId);

		int chkStartDate = studentAssignmentService.chkStartandEndDateOfAssignment(username, assignment.getId());

		List<String> getAllSapIdsOfStudentFromAssignment = studentAssignmentService
				.getAllSapIdsOfStudentFromAssignment(assignmentId, assignmentSubmission.getGroupId());

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		redirectAttrs.addAttribute("id", assignment.getId());
		try {
			//Audit change start
			Tika tika = new Tika();
			  String detectedType = tika.detect(file.getBytes());
			if (file.getOriginalFilename().contains(".")) {
				Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
				logger.info("length--->"+count);
				if (count > 1 || count == 0) {
					setError(redirectAttrs, "File uploaded is invalid!");
					return "redirect:/submitAssignmentForm";
				}else {
					logger.info("extension--->"+extension);
					if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
							|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
						setError(redirectAttrs, "File uploaded is invalid!");
						return "redirect:/submitAssignmentForm";
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
						} else {
							setError(redirectAttrs, "File uploaded is invalid!");
							return "redirect:/submitAssignmentForm";
						}
					}
				}
			}else {
				setError(redirectAttrs, "File uploaded is invalid!");
				return "redirect:/submitAssignmentForm";
			}
			//Audit change end
			if (extension.equals("zip")) {

				File normalFile = convert(file);

				Map<String, String> mapOfFiles = unzip(normalFile.getAbsolutePath(), submissionFolder,
						getAllSapIdsOfStudentFromAssignment, assignment.getAssignmentName() + '-' + assignment.getId());

				if (mapOfFiles.containsKey("Error")) {
					setError(redirectAttrs, mapOfFiles.get("Error"));
					return "redirect:/submitAssignmentForm";
				} else if (mapOfFiles.containsKey("ErrorInUploading")) {
					setError(redirectAttrs, mapOfFiles.get("ErrorInUploading"));
					return "redirect:/submitAssignmentForm";

				} else {

					if (mapOfFiles.size() == getAllSapIdsOfStudentFromAssignment.size()) {

						if (chkStartDate == 0) {

							setNote(redirectAttrs,
									"YOUR GROUP ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY");
						}
						for (String key : mapOfFiles.keySet()) {

							Map<String, String> submissionStatus = submitAssignmentById(assignment.getId(), key,
									mapOfFiles.get(key), m, principal, redirectAttrs);

							if (submissionStatus.containsKey("Plag" + key)) {

								setError(redirectAttrs, submissionStatus.get("Plag" + key));

							}
						}
					} else {
						setError(redirectAttrs, "No. of files doesn't match with No. Of Students in the Group");
						return "redirect:/submitAssignmentForm";
					}
				}

				if (normalFile.exists())
					FileUtils.deleteQuietly(normalFile);
				if (new File(
						downloadAllFolder + File.separator + assignment.getAssignmentName() + '-' + assignment.getId())
								.exists()) {
					FileUtils.deleteQuietly(new File(downloadAllFolder + File.separator + assignment.getAssignmentName()
							+ '-' + assignment.getId()));
				}

			} else {
				setError(redirectAttrs, "Submitted File is not a zip file");

				return "redirect:/submitAssignmentForm";
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		setSuccess(redirectAttrs, "Assignment Submitted Successfully");

		m.addAttribute("assignment", assignment);

		redirectAttrs.addAttribute("courseId", assignmentSubmission.getCourseId());

		return "redirect:/viewAssignmentFinal";

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/submitAssignmentById", method = {

			RequestMethod.GET, RequestMethod.POST })
	public Map<String, String> submitAssignmentById(

			@RequestParam("assignmentId") Long assignmentId, @RequestParam("username") String username,

			@RequestParam("filePath") String filePath, Model m, Principal principal, RedirectAttributes rd) {

		m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment",

				false, false, true, true, false));
		/*
		 * StudentAssignment assignmentSubmission = studentAssignmentService
		 * .findByID(studentAssignmentId);
		 */
		Map<String, String> copyLeaksMsg = new HashMap<>();
		StudentAssignment assignmentByStudent = studentAssignmentService.findAssignmentSubmission(username,
				assignmentId);

		String submittingUser = principal.getName();

		assignmentByStudent.setSubmissionDate(Utils.getInIST());

		assignmentByStudent.setLastModifiedBy(submittingUser);
		assignmentByStudent.setStudentFilePath(filePath);
		assignmentByStudent.setFilePreviewPath(filePath);

		Assignment assignment = assignmentService.findByID(assignmentByStudent.getAssignmentId());

		String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

				.format(assignmentByStudent.getSubmissionDate());
		assignmentByStudent.setActive("Y");

		int chkStartDate = studentAssignmentService.chkStartandEndDateOfAssignment(username, assignment.getId());

		int chkStartAndEndDate = studentAssignmentService.chkStartandEndDtOfAssignment(username, assignment.getId());

		/*
		 * if (sdf.compareTo(assignment.getEndDate()) > 0) {
		 * 
		 * assignmentSubmission.setSubmissionStatus("Y");
		 * 
		 * logger.info("submssn status ----------------->  Y"); } else {
		 * 
		 * logger.info("submssn status ----------------->  N");
		 * 
		 * assignmentSubmission.setSubmissionStatus("N"); }
		 */

		if (chkStartAndEndDate > 0) {

			assignmentByStudent.setSubmissionStatus("Y");

		} else {

			assignmentByStudent.setSubmissionStatus("N");
		}

		assignmentByStudent.setStartDate(assignment.getStartDate());

		assignmentByStudent.setEndDate(assignment.getEndDate());

		if (chkStartDate == 0) {
			setNote(rd, "Assignment Submission has not started yet or deadline is missed!!");

		} else {

			if (!filePath.isEmpty()) {
				Date date = new Date();

				String errorMessage = null;

				Map<String, Integer> plagValueMap = new HashMap<String, Integer>();

				if (errorMessage == null) {

					CopyleaksAudit cplAudit = copyleaksAuditService.getRecordByUsername(username, assignment.getId());

					boolean isPresent = false;
					boolean copyleakscheck = false;
					if (cplAudit == null) {
						isPresent = false;
						copyleakscheck = true;
					} else {

						isPresent = true;
						if (cplAudit.getCount() >= 2) {
							copyleakscheck = false;

						} else {
							copyleakscheck = true;
						}

					}

					if (assignment.getPlagscanRequired().equals("Yes")
							&& assignment.getRunPlagiarism().equals("Submission") && copyleakscheck == true) {

						if (plagiarismCheck.equalsIgnoreCase("copyLeaks"))

						{ // CopyLeaks copyLeaks = new CopyLeaks();

							try {
								File storedFile = submit.multipartToFileForS3(assignmentByStudent.getStudentFilePath());
								String scanId = app.replaceAll("-", "").toLowerCase() +"-"+ assignment.getId() +"-"+ username+"-"+RandomStringUtils.randomNumeric(2, 2);
								Map<String,String> scanStatus = copyLeaks.scanFileForCopyleaks(copyleaksId,copyleaskKey,storedFile,scanId);
								if(scanStatus.containsKey("Error")){
									setError(rd,"For "+username+ ": "+scanStatus.get("Error")+"Please submit the file again.");
								}else {
										studentAssignmentAuditService.insert(assignmentByStudent);
										StudentAssignment auditAssignmentSubmission = studentAssignmentAuditService.findAssignmentSubmission(username,assignment.getId());
									
									logger.info("Waiting...");
									long waitingTime = 0;
									while(null == auditAssignmentSubmission.getThreshold()) {
											Thread.sleep(5000);
											waitingTime = waitingTime + 5000;
											logger.info("Waiting..."+auditAssignmentSubmission.getThreshold());
											//wait for 4.30 mins only
											if(waitingTime > 258000) {
												break;
											}
											auditAssignmentSubmission = studentAssignmentAuditService.findAssignmentSubmission(username,assignment.getId());
											logger.info("Checking..."+auditAssignmentSubmission.getThreshold());
									}
									logger.info("End..."+auditAssignmentSubmission.getThreshold());
									
									if(null != auditAssignmentSubmission.getThreshold()) {
										logger.info("Threshold--->"+auditAssignmentSubmission.getThreshold());
										if (assignment.getThreshold() <= auditAssignmentSubmission.getThreshold()) {
											String key = "Plag" + username;
											setError(rd, "Uploaded File does not fall under plag threshold " + auditAssignmentSubmission.getThreshold()
													+ "% copied from " + auditAssignmentSubmission.getUrl());
											copyLeaksMsg.put(key, username + " has a file which fall under plag threshold "
													+ auditAssignmentSubmission.getThreshold() + "% copied from " + auditAssignmentSubmission.getUrl());
										}else{
											String key = "NoPlag" + username;
											assignmentByStudent.setThreshold(auditAssignmentSubmission.getThreshold());
											assignmentByStudent.setUrl(auditAssignmentSubmission.getUrl());
											studentAssignmentService.update(assignmentByStudent);
											if (chkStartAndEndDate == 0) {
												setNote(rd,"YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY");
											} else {
												setSuccess(rd, "Assigment Answer file uploaded successfully");
											}
											copyLeaksMsg.put(key, "No Plagiarism");
										}	
									}else {
										setError(rd, "File takes too long time for plagiarism.");
									}
							}
								

								FileUtils.deleteQuietly(storedFile);
							} catch (Exception e) {
								logger.error(e.getMessage());
							}
						}

					} else {
						try {

							studentAssignmentService.update(assignmentByStudent);
							studentAssignmentAuditService.insert(assignmentByStudent); //
							setSuccess(rd, "Assigment Answer file uploaded successfully");

							String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

									.format(assignmentByStudent.getSubmissionDate());

							if (chkStartAndEndDate == 0) {
								setNote(rd,

										"YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY"

								);
							} else {
								setSuccess(rd, "Assigment Answer file uploaded successfully");
							}
						} catch (Exception e) {
							logger.error(e.getMessage());
						}

					}

				} else {
					setError(m, errorMessage);
				}

			} else {
				setError(rd, "File is empty, please upload correct file.");
			}

		}

		if (assignment.getRunPlagiarism() != null) {
			if (assignment.getRunPlagiarism().equals("Manual")) {
				m.addAttribute("showCheckForPlagiarism", true);
			}
		}

		m.addAttribute("assignment", assignment);
		assignmentByStudent =

				studentAssignmentService.findAssignmentSubmission(username,

						assignmentByStudent.getId());

		m.addAttribute("assignmentSubmission", assignmentByStudent);

		return copyLeaksMsg;
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/evaluateByStudentFormForModule", method = { RequestMethod.GET, RequestMethod.POST })
	public String evaluateByStudentFormForModule(Model m, Long id, @ModelAttribute StudentAssignment assignment,
			Principal principal, @RequestParam(name = "moduleId", required = false, defaultValue = "") String moduleId,
			HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignmentList", "Search Assignments", false, false));
		m.addAttribute("assignment", assignment);

		if (moduleId == null || moduleId.isEmpty()) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord").equals("")) {

			} else {
				request.getSession().removeAttribute("courseRecord");
			}

		}

		/*
		 * m.addAttribute( "allCourses", courseService.findByUserActive(username,
		 * userdetails1.getProgramName()));
		 */
		List<Course> moduleList = courseService.findModulesByUsername(username,
				Long.parseLong(userdetails1.getProgramId()));
		m.addAttribute("moduleList", moduleList);
		m.addAttribute("preAssigment", new ArrayList());

		/*
		 * Assignment assignments = assignmentService.findByID(id); assignment.setCourse
		 * (courseService.findByID(assignments.getCourseId()));
		 */
		m.addAttribute("allAssignments", assignmentService.findAll());
		return "assignment/evaluateByStudentForModule";
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/getAssigmentByModule", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAssigmentByModule(@RequestParam(name = "moduleId") String moduleId,
			Principal principal, @ModelAttribute Assignment assignment) {
		String json = "";
		String username = principal.getName();
		List<Assignment> assigments = new ArrayList<Assignment>();
		if (assignment.getModuleId() != null)

		{

			assigments = assignmentService.findByFacultyAndModuleActive_new(username, Long.valueOf(moduleId));

		}
		/*
		 * List<Assignment> assigments = assignmentService
		 * .findByFacultyAndCourseActive(username, Long.valueOf(courseId));
		 */
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

		// logger.info("json" + json);
		return json;

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/evaluateByStudentForModule", method = { RequestMethod.GET, RequestMethod.POST })
	public String evaluateByStudentForModule(@RequestParam(required = false, defaultValue = "1") int pageNo,
			Principal principal, Model m, @ModelAttribute Assignment assignment, Long id) {
		m.addAttribute("webPage",
				new WebPage("evaluateAssignment", "Search Assignment To Evaluate", true, false, true, true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		logger.info("AssignmentId---------->" + id);
		logger.info("AssignmentId---------->" + assignment.getId());
		m.addAttribute("assignmentId", id);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<Course> moduleList = courseService.findModulesByUsername(username,
				Long.parseLong(userdetails1.getProgramId()));
		m.addAttribute("moduleList", moduleList);
		m.addAttribute("preAssigment", new ArrayList());

		try {
			StudentAssignment sa = new StudentAssignment();
			sa.setEvaluatedBy(username);

			// Page<StudentAssignment> page =
			// studentAssignmentService.findAssignmentForEvaluation(assignment,
			// pageNo, pageSize, username);

			List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignment.getId());
			List<StudentAssignment> assignmentsList = studentAssignmentService.getStudentsBasedOnAssignmentForModule(sa,
					childAssignmentId);
			// Boolean check = false;
			Boolean plagCheck = false;
//			for (StudentAssignment ss : assignmentsList) {
//
//				if (ss.getStudentFilePath().endsWith(".pdf")) {
//
//					check = true;
//
//				} else {
//
//					check = false;
//					break;
//				}
//
//			}
			for (StudentAssignment ss : assignmentsList) {
				logger.info("Threshold--->" + ss.getThreshold());
				if (ss.getThreshold() != null) {
					plagCheck = true;
				} else {
					plagCheck = false;
					break;
				}
			}
			m.addAttribute("plagCheck", plagCheck);
//			if (check) {
//				m.addAttribute("checkCopy", true);
//
//			} else {
//				m.addAttribute("checkCopy", false);
//			}

			List<Assignment> assigments = new ArrayList();
			if (assignment.getCourseId() != null)
				assigments = assignmentService.findByFacultyAndCourseActive(username,
						Long.valueOf(assignment.getCourseId()));

			for (StudentAssignment uc : assignmentsList) {
				User u1 = userService.findByUserName(uc.getUsername());
				uc.setRollNo(u1.getRollNo());
				String studentFilePath = StringUtils.trimToNull(uc.getStudentFilePath());
				if (studentFilePath != null) {
					uc.setShowStudentFileDownload("true");
				} else {
					uc.setShowStudentFileDownload("false");
				}
				Assignment a = assignmentService.findByID(uc.getAssignmentId());
				assignment.setAssignmentName(a.getAssignmentName());
				String filePath = StringUtils.trimToNull(a.getFilePath());
				if (filePath != null) {
					uc.setShowFileDownload("true");
				} else {
					uc.setShowFileDownload("false");
				}
				assignmentsList.set(assignmentsList.indexOf(uc), uc);
			}

			m.addAttribute("assignmentsList", assignmentsList);
			m.addAttribute("q", getQueryString(assignment));
			m.addAttribute("assignment", assignment);
			m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));
			m.addAttribute("preAssigments", assigments);

			if (assignmentsList == null || assignmentsList.size() == 0) {
				setNote(m, "No Assignments found");
			}

			/*
			 * List<StudentAssignment> students = studentAssignmentService
			 * .getStudentsBasedOnAssignment(id); m.addAttribute("students", students);
			 */

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Student List");
		}
		logger.info("AssignmentId obj---------->" + assignment.getId());
		return "assignment/evaluateByStudentForModule";
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/checkCopiedAssignmentForModule", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView checkCopiedAssignmentForModule(Principal principal, Model m, @RequestParam Long assignmentId,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		String filePath = "";

		List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignmentId);
		List<StudentAssignment> studentAssignmentFilesList = studentAssignmentService
				.getStudentFilesForModule(childAssignmentId);
		Assignment assignment = assignmentService.findByID(assignmentId);
		Course c = courseService.findByModuleIdAndAcadYear(String.valueOf(assignment.getModuleId()),
				String.valueOf(assignment.getAcadYear()));
		// Course c = courseService.findByID(assignment.getCourseId());
		StudentAssignment searchBean = new StudentAssignment();
		if (assignment.getThreshold() != null) {
			searchBean.setMinMatchPercent(assignment.getThreshold());
		} else {
			searchBean.setMinMatchPercent(50);
		}
		searchBean.setCourseName(c.getModuleName());
		searchBean.setAssignmentName(assignment.getAssignmentName());

		/*
		 * StudentAssignment questionFileBean = new StudentAssignment();
		 * questionFileBean.setFilePath(assignment.getFilePath());
		 */
		String projectUrl = "";
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			StudentAssignment questionFileBean = new StudentAssignment();
			if (assignment.getFilePath() != null) {
				questionFileBean.setFilePath(assignment.getFilePath());
			} else {
				String fileName = assignment.getAssignmentName() + " questionfile.pdf";
				String qFilePath = downloadAllFolder + File.separator + fileName;
				File folderPath = new File(downloadAllFolder);
				if (!folderPath.exists()) {
					folderPath.mkdirs();
				}

				PdfWriter writer = null;
				Document document = new Document();
				writer = PdfWriter.getInstance(document, new FileOutputStream(qFilePath));

				document.open();
				Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
				Chunk chunk = new Chunk(assignment.getAssignmentText(), font);

				document.add(chunk);
				document.close();

				File qFile = new File(qFilePath);
				amazonS3ClientService.uploadFile(qFile, assignmentFolder);
				String path = assignmentFolder + "/" + qFile.getName();
				questionFileBean.setFilePath(path);
				assignment.setFilePath(path);
				assignment.setFilePreviewPath(path);
				assignmentService.update(assignment);
				FileUtils.deleteQuietly(qFile);

			}
			File ip = new File(copyCaseHelper.checkCopyCases(studentAssignmentFilesList, searchBean, questionFileBean));
			filePath = ip.getAbsolutePath();
			if (StringUtils.isEmpty(filePath)) {
				request.setAttribute("error", "true");
				request.setAttribute("errorMessage", "Error in downloading file.");
			}

			// get absolute path of the application
			ServletContext context = request.getSession().getServletContext();
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
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// get output stream of the response
			outStream = response.getOutputStream();

			IOUtils.copy(inputStream, outStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m.addAttribute("assignment", assignment);

		return null;
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/checkForPlagiarismAllForModule", method = {

			RequestMethod.GET, RequestMethod.POST })
	public String checkForPlagiarismAllForModule(Model m, Principal principal, @RequestParam Long assignmentId) {
		m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment", false, false, true, true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignmentId);
		Assignment assignment = assignmentService.findByID(assignmentId);
		m.addAttribute("assignment", assignment);
		List<StudentAssignment> students = studentAssignmentService
				.findStudentsForPlagiarismCheckForModule(childAssignmentId);

		if (plagiarismCheck.equalsIgnoreCase("copyLeaks"))

		{ // CopyLeaks copyLeaks = new CopyLeaks();

			CopyleaksAudit cplAudit = copyleaksAuditService.getRecordByUsername(username, assignmentId);

			boolean isPresent = false;
			boolean copyleakscheck = false;
			if (cplAudit == null) {
				isPresent = false;
				copyleakscheck = true;
			} else {

				isPresent = true;
				if (cplAudit.getCount() >= 2) {
					copyleakscheck = false;

				} else {
					copyleakscheck = true;
				}

			}

			Integer totalCreditUsed = 0;

			for (StudentAssignment sa : students) {

				Map<String, Integer> plagValueMap = new HashMap<String, Integer>();
				Long saId = sa.getId();

				StudentAssignment assignmentSubmission = studentAssignmentService.findByID(saId);

				if (copyleakscheck == true) {

					try {
						if (assignmentSubmission.getThreshold() == null) {

							File storedFile = submit.multipartToFile(assignmentSubmission.getStudentFilePath());
							plagValueMap = copyLeaks.scan(copyleaksId, copyleaskKey, storedFile);
							String url = "";
							Integer plagValue = 0;
							Integer creditUsed = 0;
							for (String key : plagValueMap.keySet()) {
								url = key;
								if (url != null) {

								} else {
									url = "Copyleaks internal database";
								}

								plagValue = plagValueMap.get(key);

								if (url == "creditUsed") {

									creditUsed = plagValue;
									totalCreditUsed = totalCreditUsed + creditUsed;

								} else {
									assignmentSubmission.setUrl(url);
									assignmentSubmission.setThreshold(plagValue);
								}

								/*
								 * assignmentSubmission.setUrl(url);
								 * assignmentSubmission.setThreshold(plagValue);
								 */

							}

							// logger.info("plagValue--" + plagValueMap);

							studentAssignmentService.update(assignmentSubmission);

						} else {

						}

					} catch (Exception e) {
						logger.error(e.getMessage());
					}

				}

			}

			if (isPresent == false) {

				CopyleaksAudit cpl = new CopyleaksAudit();
				cpl.setAssignmentId(assignmentId);
				cpl.setUsername(username);
				cpl.setCount(1);
				cpl.setCreditUsed(totalCreditUsed);
				cpl.setCreatedBy(username);
				cpl.setLastModifiedBy(username);

				copyleaksAuditService.insert(cpl);

			} else {

				cplAudit.setCount(cplAudit.getCount() + 1);
				cplAudit.setCreditUsed(cplAudit.getCreditUsed() + totalCreditUsed);

				copyleaksAuditService.update(cplAudit);

			}

		}
		/*
		 * ModelAndView mav = new ModelAndView(); mav.setViewName(evaluateByStudent(1,
		 * principal, m, assignment, assignmentId)); return mav;
		 */

		return (evaluateByStudentForModule(1, principal, m, assignment, assignmentId));

		// return "redirect:/evaluateByStudent";

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/searchAssignmentToEvaluateForModule", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchAssignmentToEvaluateForModule(@RequestParam(required = false, defaultValue = "1") int pageNo,
			Principal principal, Model m, @ModelAttribute("assignment") StudentAssignment assignment,
			// @RequestParam(name = "courseId", required = false, defaultValue = "") String
			// courseId,
			HttpServletRequest request) {
		m.addAttribute("webPage",
				new WebPage("evaluateAssignment", "Search Assignment To Evaluate", true, false, true, true, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);

		String acadSession = u1.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<Course> moduleList = courseService.findModulesByUsername(username,
				Long.parseLong(userdetails1.getProgramId()));
		m.addAttribute("moduleList", moduleList);
		String json = "";
		List<Assignment> assigments = new ArrayList<Assignment>();
		if (assignment.getModuleId() != null) {
			assigments = assignmentService.findByFacultyAndModuleActive_new(username,
					Long.valueOf(assignment.getModuleId()));
		}
		logger.info("AssignmentList---->" + assigments);
		m.addAttribute("preAssigments", assigments);
		try {

			/*
			 * if (courseId == null || courseId.isEmpty()) { if
			 * (request.getSession().getAttribute("courseRecord") == null ||
			 * request.getSession().getAttribute("courseRecord") .equals("")) {
			 * 
			 * } else { request.getSession().removeAttribute("courseRecord"); }
			 * 
			 * }
			 */
			assignment.setEvaluatedBy(username);
			// assignment.setSubmissionStatus("Y");
			/*
			 * List<StudentAssignment> assignmentsList = studentAssignmentService
			 * .findAssignmentForEvaluation(assignment, username);
			 */
			List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignment.getId());
			List<StudentAssignment> assignmentsList = studentAssignmentService
					.getStudentsBasedOnAssignmentForModule(assignment, childAssignmentId);
			for (StudentAssignment sa : assignmentsList) {
				User u = userService.findByUserName(sa.getUsername());
				String firstName = u.getFirstname();
				String lastName = u.getLastname();
				sa.setFirstname(firstName);
				sa.setLastname(lastName);

				sa.setRollNo(u.getRollNo());
				String studentFilePath = StringUtils.trimToNull(sa.getStudentFilePath());
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
					sa.setShowFileDownload("false");
				}

				assignmentsList.set(assignmentsList.indexOf(sa), sa);

			}

			m.addAttribute("assignmentsList", assignmentsList);
			m.addAttribute("q", getQueryString(assignment));
			m.addAttribute("assignment", assignment);
			m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));

			if (assignmentsList == null || assignmentsList.size() == 0) {
				setNote(m, "No Assignments found");
			}

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Assignment List");
		}

		return "assignment/evaluateByStudentForModule";
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/copyLeaksStudentForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String copyLeaksStudentForm(Model m) {
		m.addAttribute("webPage", new WebPage("copyLeaksAssignment", "Copy Leaks Assignment", false, false));
		m.addAttribute("leaks", new CopyleaksAudit());
		return "assignment/studentAssignCopyLeaks";
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/searchStudentCopyLeaksAudit", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchStudentCopyLeaksAudit(@ModelAttribute("leaks") CopyleaksAudit leaks, Model m) {
		m.addAttribute("webPage", new WebPage("copyLeaksAssignment", "Copy Leaks Assignment", false, false));
		List<CopyleaksAudit> leaksList = copyleaksAuditService.getRecordsByUsername(leaks.getUsername());
		m.addAttribute("userList", leaksList);
		m.addAttribute("leaks", leaks);
		return "assignment/studentAssignCopyLeaks";
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/deleteStudentCopyLeaks", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteStudentCopyLeaks(@RequestParam String id, @RequestParam String username, Principal principal,
			Model m, RedirectAttributes r) {
		copyleaksAuditService.deleteRecordById(id);
		CopyleaksAudit cp = new CopyleaksAudit();
		cp.setUsername(username);
		/* r.addAttribute("leaks", cp); */
		return searchStudentCopyLeaksAudit(cp, m);
	}
	@Secured({ "ROLE_SUPPORT_ADMIN","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/getAssignmentStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public String getAssignmentStatus(Model m) {

		List<Assignment> allAsignmentCount = assignmentService.getCurrentAllAssignmentCount();
		m.addAttribute("allAssignment", allAsignmentCount);
		if (allAsignmentCount.size() == 0) {
			setNote(m, "No Records Found!");
		}

		m.addAttribute("assignment", new Assignment());

		return "assignment/getAssignmentStatus";
	}
	@Secured({ "ROLE_SUPPORT_ADMIN","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "getAssignmentCount", method = { RequestMethod.GET, RequestMethod.POST })
	public String getAssigmentReport(@ModelAttribute Assignment assignment, Model m) {

		List<Assignment> allAsignmentCount = assignmentService.getAllAssignmentCount(assignment.getStartDate(),
				assignment.getEndDate());
		m.addAttribute("allAssignment", allAsignmentCount);
		m.addAttribute("assignment", assignment);
		if (allAsignmentCount.size() == 0) {
			setNote(m, "No Records Found!");
		}

		return "assignment/getAssignmentStatus";

	}
	@Secured({ "ROLE_SUPPORT_ADMIN","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "getLateSubmittedDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public String getLateSubmittedDetails(@RequestParam String id, Model m) {
		List<Assignment> aList = new ArrayList<>();
		if (null != id) {
			aList = assignmentService.getLateSubmittedStudentsByAssignId(id);
		}
		if (aList.size() == 0) {
			setNote(m, "No Records Found!");
		}
		//PETER START 14/07/2021
				m.addAttribute("assignmentId",id); 
				m.addAttribute("assign",new Assignment());
		//PETER END

		m.addAttribute("assignmentList", aList);
		return "assignment/getLateSubmittedStudents";

	}

	public String uploadAssignmentSubmissionFileForS3(StudentAssignment bean, MultipartFile file) {

		String errorMessage = null;
		String filePath = null;

		try {
			filePath = submissionFolder;

			Map<String, String> s3FileNameMap = amazonS3ClientService.uploadFileToS3BucketWithRandomString(file,
					filePath);
			logger.info("map--->" + s3FileNameMap);
			if (s3FileNameMap.containsKey("SUCCESS")) {
				String s3FileName = s3FileNameMap.get("SUCCESS");
				filePath = filePath + "/" + s3FileName;
				if (bean.getStudentFilePath() != null) {
					bean.setStudentFilePath(bean.getStudentFilePath() + "," + filePath);
					bean.setFilePreviewPath(bean.getFilePreviewPath() + "," + filePath);
				} else {
					bean.setStudentFilePath(filePath);
					bean.setFilePreviewPath(filePath);
				}
			} else {
				throw new Exception("Error in uploading Assignment file");
			}
		} catch (Exception e) {
			errorMessage = "Error in uploading Assignment file : " + e.getMessage();
			logger.error("Exception" + errorMessage, e);
		}

		return errorMessage;
	}
	
	@Secured({ "ROLE_SUPPORT_ADMIN","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/uploadStudentAssignmentRemarks", method = {RequestMethod.GET, RequestMethod.POST })
	public String uploadStudentAssignmentRemarks(@RequestParam("assignmentId") Long assignmentId,
			@RequestParam("file") MultipartFile file, Model m, Principal principal, RedirectAttributes redirectAttrs) {
		
		Assignment assignment = assignmentService.findByID(assignmentId);
		//String username = principal.getName();

		//StudentAssignment assignmentSubmission = studentAssignmentService.findAssignmentSubmission(username, assignmentId);

		List<String> studentsUsernameList = studentAssignmentService.getAllSapIdsOfStudentFromAssignmentForRemark(assignmentId);
		
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		redirectAttrs.addAttribute("id", assignment.getId());
		redirectAttrs.addAttribute("courseId", assignment.getCourseId());
		List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignment.getId());
		if (file.getOriginalFilename().contains(".")) {
			Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
			logger.info("length--->"+count);
			if (count > 1 || count == 0) {
				setError(redirectAttrs, "File uploaded is invalid!");
				if(childAssignmentId.size() == 0) {
					return "redirect:/evaluateByStudent";
				}else {
					return "redirect:/evaluateByStudentForModule";
				}
			}
		}else {
			setError(redirectAttrs, "File uploaded is invalid!");
			if(childAssignmentId.size() == 0) {
				return "redirect:/evaluateByStudent";
			}else {
				return "redirect:/evaluateByStudentForModule";
			}
		}
		try {
			//List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignment.getId());
			if(childAssignmentId.size() == 0) {
				if (extension.equals("zip")) {
					File normalFile = convert(file);
					Map<String, String> mapOfFiles = unzipForRemarkFile(normalFile.getAbsolutePath(), downloadAllFolder,
							studentsUsernameList, assignment.getAssignmentName() + '-' + assignment.getId() + "/Remarks");
					if (mapOfFiles.containsKey("ErrorForRemark")) {
						setError(redirectAttrs, mapOfFiles.get("ErrorForRemark"));
						return "redirect:/evaluateByStudent";
					} else if (mapOfFiles.containsKey("ErrorInUploading")) {
						setError(redirectAttrs, mapOfFiles.get("ErrorInUploading"));
						return "redirect:/evaluateByStudent";
					} 
	
					if (normalFile.exists())
						FileUtils.deleteQuietly(normalFile);
					if (new File(
							downloadAllFolder + File.separator + assignment.getAssignmentName() + '-' + assignment.getId())
									.exists()) {
						FileUtils.deleteQuietly(new File(downloadAllFolder + File.separator + assignment.getAssignmentName()
								+ '-' + assignment.getId()));
					}
				}
				setSuccess(redirectAttrs, "Student Assignment Remarks File Uplaoded Successfully");
				return "redirect:/evaluateByStudent";
			}else {
				if (extension.equals("zip")) {
					File normalFile = convert(file);
					Map<String, String> mapOfFiles = unzipForRemarkFile(normalFile.getAbsolutePath(), downloadAllFolder,
							studentsUsernameList, assignment.getAssignmentName() + '-' + assignment.getId() + "/Remarks");
					if (mapOfFiles.containsKey("ErrorForRemark")) {
						setError(redirectAttrs, mapOfFiles.get("ErrorForRemark"));
						return "redirect:/evaluateByStudentForModule";
					} else if (mapOfFiles.containsKey("ErrorInUploading")) {
						setError(redirectAttrs, mapOfFiles.get("ErrorInUploading"));
						return "redirect:/evaluateByStudentForModule";
					} 
	
					if (normalFile.exists())
						FileUtils.deleteQuietly(normalFile);
					if (new File(
							downloadAllFolder + File.separator + assignment.getAssignmentName() + '-' + assignment.getId())
									.exists()) {
						FileUtils.deleteQuietly(new File(downloadAllFolder + File.separator + assignment.getAssignmentName()
								+ '-' + assignment.getId()));
					}
				}
				setSuccess(redirectAttrs, "Student Assignment Remarks File Uplaoded Successfully");
				return "redirect:/evaluateByStudentForModule";
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
			if(childAssignmentId.size() == 0) {
				return "redirect:/evaluateByStudent";
			}else {
				return "redirect:/evaluateByStudentForModule";
			}
		}
		
	}
	
	public Map<String, String> unzipForRemarkFile(String zipFilePath, String destDir,
			List<String> listOfStudents, String assignmentNameWithId) {

		// create output directory if it doesn't exist
		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		Map<String, String> fileMap = new HashMap<>();
		try {
			zipFilePath = zipFilePath.replace("\\", "/");
			System.out.println("zipFilePath-----> "+zipFilePath);
			
			fis = new FileInputStream(zipFilePath);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();

				System.out.println("FilePath_Doubt-----> "+destDir +"/"
						+ assignmentNameWithId + "/"+ fileName);
				
				File newFile = new File(destDir +"/"
						+ assignmentNameWithId + "/"+ fileName);
				String fileNamewithoutExtn = FilenameUtils
						.removeExtension(newFile.getName());

				if (listOfStudents.contains(fileNamewithoutExtn)) {

					fileMap.put(fileNamewithoutExtn, newFile.getPath());
				} else {
					fileMap.put("Error", fileNamewithoutExtn + "does not present in a group");
					fileMap.put("ErrorForRemark", fileNamewithoutExtn + "does not allocated/submitted assignment.");
				}
				
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zis.closeEntry();
				ze = zis.getNextEntry();
				System.out.println("unzip_aws_path-----> "+submissionFolder +"/"+ assignmentNameWithId);
				amazonS3ClientService.uploadFile(newFile, submissionFolder +"/"+ assignmentNameWithId);
				//Email
				List<String> userList = new ArrayList<String>();
				String assignemtId = assignmentNameWithId.substring((assignmentNameWithId.lastIndexOf("-") + 1), (assignmentNameWithId.lastIndexOf("/")));
				Assignment a = assignmentService.findByID(Long.valueOf(assignemtId));
				String subject = "Assignment Name : " + a.getAssignmentName();
				Map<String, Map<String, String>> result = null;
				String notificationEmailMessage = "<html><body>"
						+ "Your Assignemnt Evaluated and Remark File is uploaded for assignemnt name "+a.getAssignmentName()+" <br>"
						//+ "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
						//+ "ICA Marks is published.<br>"
						+ "<b>Note: </b>To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
						+ "This is auto-generated email, do not reply on this.</body></html>";
				
					userList.add(fileNamewithoutExtn);
					logger.info("fileNamewithoutExtn---->"+fileNamewithoutExtn);
					if (!userList.isEmpty()) {
						result = userService.findEmailByUserName(userList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}	
			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileMap;
	}
	@Secured({ "ROLE_SUPPORT_ADMIN","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/downloadStudentAssignmentRemarkFile", method = {RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadStudentAssignmentRemarkFile(@RequestParam("assignmentId") Long assignmentId,
			 Model m, Principal principal, RedirectAttributes redirectAttrs) {
		
		String username = principal.getName();
		Assignment assignment = assignmentService.findByID(assignmentId);
		ByteArrayResource resource = null;
		File downloadFile = null;
		byte[] data = null;
		String remarkFilePath = amazonS3ClientService.existsFile(submissionFolder+"/"+assignment.getAssignmentName()+"-"+assignment.getId()+"/", username);
		if(remarkFilePath.equals("")) {
			if(null != assignment.getParentModuleId()) {
				Assignment parentAssignment = assignmentService.findByID(Long.valueOf(assignment.getParentModuleId()));
				remarkFilePath = amazonS3ClientService.existsFile(submissionFolder+"/"+parentAssignment.getAssignmentName()+"-"+parentAssignment.getId()+"/Remarks", username);
				downloadFile = new File(remarkFilePath);
				String path = "";
				if(remarkFilePath.startsWith("/")) {
					path = StringUtils.substring(remarkFilePath, 1);
				}else {
					path = remarkFilePath;
				}
				path = path.replace("/\\", "/");
				path = path.replace("\\\\","/");
				path = path.replace("\\","/");
				path = path.replace("//","/");
				data = amazonS3ClientService.getFile(path);
				logger.info("data"+data.length);
				resource = new ByteArrayResource(data);
			}
		}else {
			downloadFile = new File(remarkFilePath);
			String path = "";
			if(remarkFilePath.startsWith("/")) {
				path = StringUtils.substring(remarkFilePath, 1);
			}else {
				path = remarkFilePath;
			}
			path = path.replace("/\\", "/");
			path = path.replace("\\\\","/");
			path = path.replace("\\","/");
			path = path.replace("//","/");
			data = amazonS3ClientService.getFile(path);
			logger.info("data"+data.length);
			resource = new ByteArrayResource(data);
		}
		return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
		.header("Content-disposition", "attachment; filename=\"" + downloadFile.getName() + "\"")
		.body(resource);
	}

	
	
	
	@RequestMapping(value = "/uploadStudentAssignmentMarksExcel", method = { RequestMethod.POST })
	public String uploadStudentAssignmentMarksExcel(@ModelAttribute Test test, @RequestParam("file") MultipartFile file,
			 @RequestParam String assignmentId, Model m, RedirectAttributes redirectAttributes,
			Principal principal) {

		
		// redirectAttributes.addAttribute("icaId", teeId);
		//redirectAttributes.addAttribute("teeId", assignmenmtId);
		try {
			String username = principal.getName();
			Assignment assignment = assignmentService.findByID(Long.valueOf(assignmentId));

			List<String> assignmentlist = new ArrayList<>();
			boolean isAllSubmitted = true;
			List<String> validateHeaders = new ArrayList<String>(
					Arrays.asList("Sr no.", "Student User Name", "Roll No.", "Faculty Id", "Group Id", "Assignment Name","Threshold","Submission Status","Submission Date"
							,"Evaluation Status","Save Score","Save Remarks","Save Low Score Reason"));
//			List<String> validateHeaders = new ArrayList<String>(
//					Arrays.asList("Student User Name","Save Score","Save Remarks","Save Low Score Reason"));
//			// int count=3;

			logger.info("headers are--->" + validateHeaders);
			ExcelReader excelReader = new ExcelReader();

		//	List<Map<String, Object>> maps = excelReader.readIcaExcelFileUsingColumnHeader(file, headers);
			List<Map<String, Object>> maps = excelReader
					.readExcelFileUsingColumnHeader(file, validateHeaders);
			logger.info("maps received   " + maps);

			List<String> excelUserList = new ArrayList<>();
			
			List<Map<String, Object>> copy = new ArrayList<>();
			for (Map<String, Object> mapper : maps) {
				logger.info(mapper);
				if (mapper.get("Error") != null) {
					
					logger.info("elas"+mapper.get("Error"));
					setError(redirectAttributes, "Error--->" + mapper.get("Error"));
					//redirectAttributes.addAttribute("assignment", assignment);
					return "redirect:/evaluateByStudent?id="+assignmentId;
					
				} else {
						
						
					if(!mapper.get("Save Score").toString().matches("[0-9.]+") || !mapper.get("Save Score").toString().matches("[0-9.,]+") 
						|| mapper.get("Save Score").toString().isEmpty() || !NumberUtils.isNumber(mapper.get("Save Score").toString()))
					{
						logger.info("inside");
						setError(redirectAttributes, "Entered marks for student is not valid "+mapper.get("Student User Name"));
						return "redirect:/evaluateByStudent?id="+assignmentId;
					}
				//	if(mapper.get("Save Score").toString().equalsIgnoreCase("")) {
						Integer totalMarks = Integer.parseInt((String) mapper.get("Save Score"));
						logger.info("assignment.getMaxScore() marks  "+(Integer.parseInt(assignment.getMaxScore()) < totalMarks));
						if (Integer.parseInt(assignment.getMaxScore()) < totalMarks) {
							setError(redirectAttributes, "Entered marks for student is not valid  "+mapper.get("Student User Name"));
							return "redirect:/evaluateByStudent?id="+assignmentId;
				//		}
					}
					
									
				}
			}
			List<Long> childAssignmentId = new ArrayList<Long>();
			childAssignmentId.add(Long.valueOf(assignmentId));
			for (Map<String, Object> mapper : maps) 
			{
				StudentAssignment sa = new StudentAssignment();
				sa.setScore(mapper.get("Save Score").toString());
				
				sa.setRemarks(mapper.get("Save Remarks").toString());
				sa.setLowScoreReason(mapper.get("Save Low Score Reason").toString());
				sa.setUsername(mapper.get("Student User Name").toString());
				sa.setAssignmentId(Long.valueOf(assignmentId));
				sa.setEvaluationStatus("Y");
				sa.setEvaluatedBy(username);
				sa.setLastModifiedBy(username);
				studentAssignmentService.updateAssignmentsEvaluationMarks(sa,childAssignmentId);
				}
			
			
			
		} catch (

		Exception ex) {

			setError(redirectAttributes, "Error in uploading marks");

			logger.error("Exception", ex);
		}
		setSuccess(redirectAttributes, "Marks updates Successfully ");
		
		return "redirect:/evaluateByStudent?id="+assignmentId;

	}
	@Secured({ "ROLE_SUPPORT_ADMIN","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/uploadStudentAssignmentMarksExcelByModule", method = { RequestMethod.POST })
	public String uploadStudentAssignmentMarksExcelByModule(@ModelAttribute Test test, @RequestParam("file") MultipartFile file,
			 @RequestParam String assignmentId, Model m, RedirectAttributes redirectAttributes,
			Principal principal) {

		
		// redirectAttributes.addAttribute("icaId", teeId);
		//redirectAttributes.addAttribute("teeId", assignmenmtId);
		try {
			String username = principal.getName();
			Assignment assignment = assignmentService.findByID(Long.valueOf(assignmentId));

			List<String> assignmentlist = new ArrayList<>();
			boolean isAllSubmitted = true;
			List<String> validateHeaders = new ArrayList<String>(
					Arrays.asList("Sr no.", "Student User Name", "Roll No.", "Faculty Id", "Group Id", "Assignment Name","Threshold","Submission Status","Submission Date"
							,"Evaluation Status","Save Score","Save Remarks","Save Low Score Reason"));
//			List<String> validateHeaders = new ArrayList<String>(
//					Arrays.asList("Student User Name","Save Score","Save Remarks","Save Low Score Reason"));
//			// int count=3;

			logger.info("headers are--->" + validateHeaders);
			ExcelReader excelReader = new ExcelReader();

		//	List<Map<String, Object>> maps = excelReader.readIcaExcelFileUsingColumnHeader(file, headers);
			List<Map<String, Object>> maps = excelReader
					.readExcelFileUsingColumnHeader(file, validateHeaders);
			logger.info("maps received   " + maps);

			List<String> excelUserList = new ArrayList<>();
			
			List<Map<String, Object>> copy = new ArrayList<>();
			for (Map<String, Object> mapper : maps) {
				logger.info(mapper);
				if (mapper.get("Error") != null) {
					
					logger.info("elas"+mapper.get("Error"));
					setError(redirectAttributes, "Error--->" + mapper.get("Error"));
					//redirectAttributes.addAttribute("assignment", assignment);
					return "redirect:/evaluateByStudentFormForModule?id="+assignmentId;
					
				} else {
						
						
					if(!mapper.get("Save Score").toString().matches("[0-9.]+") || !mapper.get("Save Score").toString().matches("[0-9.,]+") 
						|| mapper.get("Save Score").toString().isEmpty() || !NumberUtils.isNumber(mapper.get("Save Score").toString()))
					{
						logger.info("inside");
						setError(redirectAttributes, "Entered marks for student is not valid "+mapper.get("Student User Name"));
						return "redirect:/evaluateByStudentFormForModule?id="+assignmentId;
					}
				//	if(mapper.get("Save Score").toString().equalsIgnoreCase("")) {
						Integer totalMarks = Integer.parseInt((String) mapper.get("Save Score"));
						logger.info("assignment.getMaxScore() marks  "+(Integer.parseInt(assignment.getMaxScore()) < totalMarks));
						if (Integer.parseInt(assignment.getMaxScore()) < totalMarks) {
							setError(redirectAttributes, "Entered marks for student is not valid  "+mapper.get("Student User Name"));
							return "redirect:/evaluateByStudentFormForModule?id="+assignmentId;
				//		}
					}
					
									
				}
			}
			
			List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignment.getId());
			if(childAssignmentId.size() == 0) {
				
				childAssignmentId.add(Long.valueOf(assignmentId));
			
			}
			for (Map<String, Object> mapper : maps) 
			{
				StudentAssignment sa = new StudentAssignment();
				sa.setScore(mapper.get("Save Score").toString());
				
				sa.setRemarks(mapper.get("Save Remarks").toString());
				sa.setLowScoreReason(mapper.get("Save Low Score Reason").toString());
				sa.setUsername(mapper.get("Student User Name").toString());
				//sa.setAssignmentId(Long.valueOf(assignmentId));
				sa.setEvaluationStatus("Y");
				sa.setEvaluatedBy(username);
				sa.setLastModifiedBy(username);
				studentAssignmentService.updateAssignmentsEvaluationMarks(sa,childAssignmentId);
			}
		
		
			
			
		} catch (

		Exception ex) {

			setError(redirectAttributes, "Error in uploading marks");

			logger.error("Exception", ex);
		}
		setSuccess(redirectAttributes, "Marks updates Successfully ");
		
		//return "redirect:/evaluateByStudentFormForModule?id="+assignmentId;
return  "redirect:/evaluateByStudentForModule?id="+assignmentId; 
	}
	
	
	
	//Hiren 29-08-2020
	@Secured({ "ROLE_SUPPORT_ADMIN","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/generateTemplateForEvaluate", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String generateTemplateForEvaluate(
	@RequestParam(name = "assignmentId") Long assignmentId,HttpServletRequest request,HttpServletResponse response) {
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		
		try {
			Assignment assignment = assignmentService.findByID(assignmentId);
			List<AssignmentConfiguration> assignConfigList = assignmentConfigurationService.findAllByAssignmentId(assignmentId);
			List<StudentAssignment> studentsListForAssignment = studentAssignmentService.getAllStudentToEvaluateAssignment(assignmentId);
			Course c = courseService.findByID(assignment.getCourseId());
		
			List<String> headers = new ArrayList<String>(Arrays.asList(
					"Roll No", "Name", "SAPID"));

			for (AssignmentConfiguration ac : assignConfigList) {
				headers.add(ac.getQuestionNumber() + "(" + ac.getMarks() + ")");
				// count++;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			

			XSSFSheet sheet = (XSSFSheet) workbook
					.createSheet("Upload Student Assignment Marks - " + c.getCourseName());
			Row r = sheet.createRow(0);

			for (int rn = 0; rn < headers.size(); rn++) {

				r.createCell(rn).setCellValue(headers.get(rn));
			}
			int i = 1;
			for (StudentAssignment sa : studentsListForAssignment) {
				Row row = sheet.createRow(i);

				row.createCell(0).setCellValue(String.valueOf(sa.getRollNo()));
				row.createCell(1).setCellValue(sa.getStudentName());
				row.createCell(2).setCellValue(sa.getUsername());
				if(!sa.getSubmissionStatus().equals("Y")) {
					int j = 3;
					for (AssignmentConfiguration ac : assignConfigList) {
						row.createCell(j).setCellValue("0");
						j++;
					}
				}
				i++;
			}
			//String folderPathS3 = baseDir + "/" + app + "/" + "assignmentUploadMarkTemp";
			String folderPath = downloadAllFolder + "/" + "assignmentUploadMarkTemp";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdirs();
			}
			String filePath = folderP.getAbsolutePath() + File.separator + "uploadMarksForAssignment" + assignmentId + ".xls";
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
			ServletContext context = request.getSession().getServletContext();
			File downloadFile = new File(filePath);

			if (!downloadFile.exists()) {
				return "Error: Dowmload file is not found";
			}
			inputStream = new FileInputStream(downloadFile);
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			logger.info("MIME type: " + mimeType);

			response.setContentLength((int) downloadFile.length());
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					downloadFile.getName());
			response.setHeader(headerKey, headerValue);
			outStream = response.getOutputStream();

			IOUtils.copy(inputStream, outStream);
			//return filePath;
		} catch (Exception ex) {
			logger.error("Exception", ex);
			return "error";
		} finally {
			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);
			if (outStream != null)
				IOUtils.closeQuietly(outStream);

		}
		return null;
	}
	
	
	@Secured({ "ROLE_SUPPORT_ADMIN","ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/uploadStudentQuestionwiseMarksExcel", method = { RequestMethod.POST })
	public String uploadStudentQuestionwiseMarksExcel(@RequestParam("file") MultipartFile file, @RequestParam Long assignmentId, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {
		
		String username = principal.getName();
		
		try {
		Assignment assignment = assignmentService.findByID(assignmentId);
		List<AssignmentConfiguration> assignConfigList = assignmentConfigurationService.findAllByAssignmentId(assignmentId);
		List<StudentAssignment> studentsListForAssignment = studentAssignmentService.getAllStudentToEvaluateAssignment(assignmentId);
		List<String> headers = new ArrayList<String>(Arrays.asList("Roll No", "Name", "SAPID"));
		for (AssignmentConfiguration ac : assignConfigList) {
			headers.add(ac.getQuestionNumber() + "(" + ac.getMarks() + ")");
		}
		Map<String, AssignmentConfiguration> assignConfigMap = new HashMap<>();
		for (AssignmentConfiguration i : assignConfigList) {
			assignConfigMap.put(i.getQuestionNumber(), i);
		}
		ExcelReader excelReader = new ExcelReader();

		List<Map<String, Object>> maps = excelReader.readIcaExcelFileUsingColumnHeader(file, headers);
		
		List<String> studentList = studentAssignmentService.getAllStudentSAPIdToEvaluateAssignment(assignmentId);
		
		List<Map<String, Object>> copy = new ArrayList<>();
		
		copy = maps.stream().filter(s -> {

			String user = (String) s.get("SAPID");
			if (studentList.contains(user)) {
				return true;
			} else {
				return false;
			}
		}).collect(Collectors.toList());
		if (studentList.size() > copy.size() || copy.size() > studentList.size()) {
			setError(redirectAttributes, "You have tampered the SAP IDs given in the template!");

			return "redirect:/evaluateByStudent?id="+assignmentId;
		}
		
		
		List<StudentAssignmentQuestionwiseMarks> studentAssignQuesList = new ArrayList<>();
		List<StudentAssignment> studentTotalMarks = new ArrayList<>();
		for (Map<String, Object> mapper : copy) {
			StudentAssignment sa = new StudentAssignment();
			if (mapper.get("Error") != null) {
				setError(redirectAttributes, (String) mapper.get("Error"));
				return "redirect:/evaluateByStudent?id="+assignmentId;
			}

			double total = 0;
			int questionCount = 0;

			for (String ac : assignConfigMap.keySet()) {
				StudentAssignmentQuestionwiseMarks saqm = new StudentAssignmentQuestionwiseMarks();
				saqm.setUsername((String) mapper.get("SAPID"));
				
				String aqcMark = mapper.get(ac + "(" + assignConfigMap.get(ac).getMarks()+ ")").toString();
				if (!excelReader.ISVALIDINPUT(aqcMark) || aqcMark.toString().isEmpty()) {
					setError(redirectAttributes,"Input Marks is not valid for student: "+ (String) mapper.get("SAPID")+ "-" + ac);
					return "redirect:/evaluateByStudent?id="+assignmentId;
				}else {
					double questionMarksForStudent = Double.parseDouble((String) mapper.get(ac+ "("
									+ assignConfigMap.get(ac).getMarks() + ")"));
					if (assignConfigMap.get(ac).getMarks() < questionMarksForStudent) {
						setError(redirectAttributes,"Invalid "+ ac + ": Marks For Student "+ (String) mapper.get("SAPID"));
						return "redirect:/evaluateByStudent?id="+assignmentId;
					}else {
						saqm.setAssignmentId(assignmentId);
						saqm.setAssignConfigId(assignConfigMap.get(ac).getId());
						saqm.setMarks(questionMarksForStudent);
						saqm.setActive("Y");
						saqm.setCreatedBy(username);
						saqm.setLastModifiedBy(username);
						studentAssignQuesList.add(saqm);
						total = total + questionMarksForStudent;
					}
				}
			}
			sa.setUsername((String) mapper.get("SAPID"));
			sa.setScore(String.valueOf(total));
			sa.setAssignmentId(assignmentId);
			sa.setEvaluatedBy(username);
			studentTotalMarks.add(sa);
		}
		
		
		//upsert
		studentAssignmentQuestionwiseMarksService.upsertBatch(studentAssignQuesList);
		for(StudentAssignment sa:studentTotalMarks) {
			studentAssignmentService.updateStudentAssignmentTotalScore(sa);
		}
		
		setSuccess(redirectAttributes,"Marks uploaded Successfully");
		}catch(Exception e) {
			logger.error("Error--->"+e);
			setError(redirectAttributes, "Error in uploading marks");
		}
		
		return "redirect:/evaluateByStudent?id="+assignmentId;
	}
	
	
	
	// Generate Hash Key Code
		@RequestMapping(value = "/submitAssignmentForCheckSum", method = {

				RequestMethod.GET, RequestMethod.POST })
		public String submitAssignmentForCheckSum(

				@ModelAttribute StudentAssignment assignmentSubmission,

				@RequestParam("file") MultipartFile file, Model m, Principal principal, RedirectAttributes rd) {

			m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment",

					false, false, true, true, false));
			String username = principal.getName();
			Long assignmentId = assignmentSubmission.getId();
			
			File folderPath = new File(submissionFolderLocal);
			if(!folderPath.exists()) {
				folderPath.mkdirs();
			}
			
			File submittedFile = new File(submissionFolderLocal + File.separator + file.getOriginalFilename());
			try {
				//Audit change start
				Tika tika = new Tika();
				  String detectedType = tika.detect(file.getBytes());
				if (file.getOriginalFilename().contains(".")) {
					Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
					logger.info("length--->"+count);
					if (count > 1 || count == 0) {
						setError(rd, "File uploaded is invalid!");
						return "redirect:/viewAssignmentFinal";
					}else {
						String extension = FilenameUtils.getExtension(file.getOriginalFilename());
						logger.info("extension--->"+extension);
						if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
								|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
							setError(rd, "File uploaded is invalid!");
							return "redirect:/viewAssignmentFinal";
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
							} else {
								setError(rd, "File uploaded is invalid!");
								return "redirect:/viewAssignmentFinal";
							}
						}
					}
				}else {
					setError(rd, "File uploaded is invalid!");
					return "redirect:/viewAssignmentFinal";
				}
				//Audit change end
				logger.info("file is:" + file.getOriginalFilename());
				logger.info("file is:" + file.getName());
				
				try (OutputStream os = new FileOutputStream(submittedFile)) {
					os.write(file.getBytes());
				}

				MessageDigest md5Digest = MessageDigest.getInstance("SHA-1");
				String hashKey = getFileChecksum(md5Digest, submittedFile);

				if (hashKey != null) {
					StudentAssignment studAssgnHashKeyDB = studentAssignmentService.getStudentAssignmentHashKey(username,
							assignmentId);
					Date keyGenDate = Utils.getInIST();
					if (studAssgnHashKeyDB == null) {

						StudentAssignment studAssgn = new StudentAssignment();
						studAssgn.setUsername(username);
						studAssgn.setAssignmentId(assignmentId);
						studAssgn.setHashKey(hashKey);
						studAssgn.setKeyGenerationTime(keyGenDate);
						studAssgn.setActive("Y");
						studAssgn.setCreatedBy(username);
						studAssgn.setLastModifiedBy(username);
						studAssgn.setCreatedDate(keyGenDate);
						studAssgn.setLastModifiedDate(keyGenDate);
						studAssgn.setIsHashKeyLateSubmitted("N");
						studentAssignmentService.insertStudentAssignmenteHashKey(studAssgn);
					} else {
						studAssgnHashKeyDB.setLastModifiedBy(username);
						studAssgnHashKeyDB.setLastModifiedDate(keyGenDate);
						studAssgnHashKeyDB.setKeyGenerationTime(keyGenDate);
						studAssgnHashKeyDB.setHashKey(hashKey);
						studAssgnHashKeyDB.setIsHashKeyLateSubmitted("N");
						studentAssignmentService.updateStudentAssignmentHashKey(studAssgnHashKeyDB);

					}
					setSuccess(rd, "Hash Key Generated Successfully!!");
				}else {
					setSuccess(rd, "HashKey Could not be generated");
				}
				
			} catch (Exception ex) {
				setError(rd, "Error Occured While Generating Hash Key");
			}
			FileUtils.deleteQuietly(submittedFile);
			rd.addAttribute("courseId", assignmentSubmission.getCourseId());

			return "redirect:/viewAssignmentFinal";
		}
		
		
		
		@RequestMapping(value = "/lateAssgnSubmissionRemarks", method = {

				RequestMethod.GET, RequestMethod.POST })
		public String lateAssgnSubmissionRemarks(

				@ModelAttribute Assignment assignmentSubmission,

			 Model m, Principal principal, RedirectAttributes rd) {

			m.addAttribute("webPage", new WebPage("assignment", "Submit Assignment",

					false, false, true, true, false));
			String username = principal.getName();
			Long assignmentId = assignmentSubmission.getId();
		
			try {
				
				
				StudentAssignment studHashKey = studentAssignmentService.getStudentAssignmentHashKey(username, assignmentId);
				Date currDate = Utils.getInIST();
				if(studHashKey!=null) {
					studHashKey.setHashKey(assignmentSubmission.getHashKey());
					studHashKey.setLateSubmRemark(assignmentSubmission.getLateSubmRemark());
					studHashKey.setIsHashKeyLateSubmitted("Y");
					studHashKey.setKeyGenerationTime(currDate);
					studHashKey.setLastModifiedBy(username);
					studHashKey.setLastModifiedDate(currDate);
					studentAssignmentService.updateStudentAssignmentHashKey(studHashKey);
				}else {
					StudentAssignment sa = new StudentAssignment();
					sa.setUsername(username);
					sa.setIsHashKeyLateSubmitted("Y");
					sa.setKeyGenerationTime(currDate);
					sa.setUsername(username);
					sa.setAssignmentId(assignmentId);
					sa.setActive("Y");
					sa.setHashKey(assignmentSubmission.getHashKey());
					sa.setLateSubmRemark(assignmentSubmission.getLateSubmRemark());
					sa.setCreatedBy(username);
					sa.setLastModifiedBy(username);
					sa.setCreatedDate(currDate);
					sa.setLastModifiedDate(currDate);
					studentAssignmentService.insertStudentAssignmenteHashKey(sa);
					
				}
				setSuccess(rd, "Hash Key & Remarks Added Successfully!!");
			} catch (Exception ex) {
				logger.error("Error",ex);
				setError(rd, "Error in adding hash key and remarks");
			}
			
			rd.addAttribute("courseId", assignmentSubmission.getCourseId());

			return "redirect:/viewAssignmentFinal";
		}

		public String getFileChecksum(MessageDigest digest, File file) throws IOException {
			// Get file input stream for reading the file content
			FileInputStream fis = new FileInputStream(file);

			// Create byte array to read data in chunks
			byte[] byteArray = new byte[1024];
			int bytesCount = 0;

			// Read file data and update in message digest
			while ((bytesCount = fis.read(byteArray)) != -1) {
				digest.update(byteArray, 0, bytesCount);
			}
			;

			// close the stream; We don't need it now.
			fis.close();

			// Get the hash's bytes
			byte[] bytes = digest.digest();

			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			// return complete hash
			return sb.toString().toUpperCase();
		}
		
		
		@RequestMapping(value = "/getStudentDetails", method = { RequestMethod.GET, RequestMethod.POST })
		public String getStudentDetails(@RequestParam(required = false) Long id, Model m, Principal principal) {
			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

	List<StudentAssignment> studentdetails=studentAssignmentService.getStudentDetails(id);	
	m.addAttribute("studentAssn", new StudentAssignment());//Peter 06/08/2021
		m.addAttribute("studentdetails", studentdetails);

			return "assignment/studentDetails";
			
		}
		
	/* For Assignment Pool Start */
	@RequestMapping(value = "/viewQuestionsForStudent", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewQuestionsForStudent(@RequestParam String assignmentId, Model m, 
			Principal principal,RedirectAttributes r) {

		String username = principal.getName();
		Token userdetails = (Token) principal;
		String ProgramName = userdetails.getProgramName();
		
		LmsVariables showDisclaimerInAssignment = lmsVariablesService.getLmsVariableBykeyword("showDisclaimerInAssignment");
		if(null != assignmentId && !("").equals(assignmentId)) {
		logger.info("assignmentId---->"+assignmentId);
		Assignment assignment = assignmentService.findByID(Long.valueOf(assignmentId));
		if (assignment != null) {
			Date currDate = Utils.getInIST();
			Date startDate = Utils.converFormatsDateAlt(assignment.getStartDate());
			if(startDate.after(currDate)) {
				setNote(r, "Can't view questions before start time!");
				return "redirect:/viewAssignmentFinal?courseId="+assignment.getCourseId();
			}
		}
		StudentAssignment sa = studentAssignmentService.findAssignmentSubmission(username, assignment.getId());
		if (userdetails.getAuthorities().contains(Role.ROLE_STUDENT) && showDisclaimerInAssignment.getValue().equals("Yes")) {
			if(sa.getIsAcceptDisclaimer().equals("Y")) {
				
				List<StudentAssignmentQuestion> questionList = new ArrayList<>();
				if("Y".equals(assignment.getRandomQuestion())) {
					questionList = studentAssignmentQuestionService.getAllByAssignIdAndUsername(assignmentId, username);
				}else {
					questionList = studentAssignmentQuestionService.getAllByAssignIdAndUsernameForNonRandom(assignmentId, username);
				}
				
				if(questionList.size() > 0 && assignment.getIsQuesConfigFromPool().equals("Y")) {
					m.addAttribute("questionList", questionList);
					return "assignment/viewQuestions";
				}else {
					setNote(r, "No questions to show!");
					return "redirect:/viewAssignmentFinal?courseId="+assignment.getCourseId();
				}
				
			}
		}
			setError(r, "Error in displaying questions!");
			return "redirect:/viewAssignmentFinal?courseId="+assignment.getCourseId();
		}else {
			setError(r, "Error in displaying questions!");
			return "redirect:/viewAssignmentFinal";
		}
		
	}
	
	@RequestMapping(value = "/acceptDiscalimer", method = RequestMethod.POST)
	public @ResponseBody String acceptDiscalimer(
			@RequestParam(required = false) String assignmentId,
			RedirectAttributes redirectAttrs, Principal principal, Model m) {
		
		String username = principal.getName();
	
		if(null != assignmentId && null != username)
		studentAssignmentService.updateDisclaimer(assignmentId, username);
	 
		return "success";
	}
	
	//Peter 13/07/2021
		@RequestMapping(value="/updateApprovalStatusBySupportAdmin" , method = { RequestMethod.GET,RequestMethod.POST})
		public String updateApprovalStatusBySupportAdmin(@RequestParam Long assignmentId, @ModelAttribute Assignment assignment) {
			studentAssignmentService.updateAssignmentApprovalStatus(assignment.getStudents(), assignmentId);
			return "redirect:/getLateSubmittedDetails?id="+assignmentId;
		}
	
	//Peter 04/08/2021
	@RequestMapping(value="/updateFilePath" , method = { RequestMethod.GET,RequestMethod.POST})
	public String updateFilePath(@RequestParam Long assignmentId, @RequestParam Long studentAssignmentId,
			@ModelAttribute StudentAssignment studentAssignment) {
		studentAssignmentService.updateFilePath(studentAssignmentId,studentAssignment.getStudentFilePath());
		return "redirect:/getStudentDetails?id="+assignmentId;
	}
}
