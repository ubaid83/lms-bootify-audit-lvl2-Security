package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.writer.MapperRemapped;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.impl.AttributeImpl.UseImpl;
import org.jsoup.select.Evaluator.IsEmpty;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;







import com.spts.lms.auth.Token;
import com.spts.lms.beans.classParticipation.ClassParticipation;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.faq.Faq;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.services.classParticipation.ClassParticipationService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.faq.FaqService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;
@Controller
public class FAQController extends BaseController {

	@Autowired
	FaqService faqService;
	@Autowired
	UserService userService;
	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;
	
	@Autowired
	CourseService courseService;

	@Autowired
	ClassParticipationService classParticipationService;
	
	@Autowired
	UserCourseService userCourseService; 

	@Autowired
	BusinessBypassRule businessBypassRule;

	private static final Logger logger = Logger.getLogger(FAQController.class);
	private static final String String = null;

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addFAQForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addFAQForm(Model m, Principal p) {
		Token userdetails1 = (Token) p;
		String username = p.getName();

		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("groups", "Upload FAQs", false,
				false));

		Faq faq = new Faq();

		m.addAttribute("faq", faq);
		
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "faq/addFAQAdmin";
		} else {
		return "faq/addFAQ";
		}



	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addFAQ", method = { RequestMethod.POST })
	public String addFAQ(@ModelAttribute Faq faq,
			@RequestParam("file") MultipartFile file, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("test", "Upload FAQs", true,
				false));
		List<String> validateHeaders = null;
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		validateHeaders = new ArrayList<String>(Arrays.asList("question",
				"answer", "questionType"));

		

		ExcelReader excelReader = new ExcelReader();

		try {
			List<Map<String, Object>> maps = excelReader
					.readExcelFileUsingColumnHeader(file, validateHeaders);
			
			
			if (maps.size() == 0) {
				setNote(m, "Excel File is empty");
			} else {

				Map<String, Object> map = maps.get(0);
				/*
				 * int firstQuestionanswer = Integer.parseInt((String) map
				 * .get("answer"));
				 */

				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {
					
						setNote(m, "Error  " + mapper.get("Error"));
					} else {
						Faq faq1 = new Faq();
						faq1.setCreatedBy(username);
						faq1.setLastModifiedBy(username);
						mapper.put("createdBy", faq1.getCreatedBy());
						mapper.put("lastModifiedBy", faq1.getLastModifiedBy());
						mapper.put("createdDate", Utils.getInIST());
						mapper.put("lastModifiedDate", Utils.getInIST());
						// mapper.put("courseId", null);
						faqService.insertUsingMap(mapper);
						setSuccess(m, "FAQ file uploaded successfully");

					}
				}

			}
			m.addAttribute("faq", faq);
		} catch (Exception ex) {
			setError(m, "Error in uploading file");
			ex.printStackTrace();
		}
		
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "faq/addFAQAdmin";
		} else {

		return "faq/addFAQ";
		}
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/viewFAQ", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewFAQ(Model m, Principal principal, @ModelAttribute Faq faq) {
		m.addAttribute("webPage", new WebPage("addTestQuestion", "Upload FAQs",
				true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("admissionsFAQs", faqService.getAdmissionsFAQs());
		m.addAttribute("academicsFAQs", faqService.getAcademicsFAQs());
		m.addAttribute("examsFAQs", faqService.getExamsFAQs());
		m.addAttribute("othersFAQs", faqService.getOthersFAQs());
		m.addAttribute("supportFAQs", faqService.getSupportFAQs());
		m.addAttribute("faq", faq);
		return "faq/faq";

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/classParticipation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String classParticipation(Model m, @RequestParam Long courseId,
			Principal principal,
			@ModelAttribute ClassParticipation classParticipation) {
		m.addAttribute("webPage", new WebPage("viewAssignment",
				"Class Participation", true, false));
		// Token userDetails = (Token) principal;
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		

		String acadSession = u1.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<ClassParticipation> students = classParticipationService
				.findStudentsForFaculty(courseId);
		// List<ClassParticipation> savedStudents =
		// classParticipationService.findAllActive();

		for (ClassParticipation uc : students) {
			User u = userService.findByUserName(uc.getUsername());
			uc.setRollNo(u.getRollNo());
			students.set(students.indexOf(uc), uc);
		}

		
		Course c = courseService.findByID(courseId);
		m.addAttribute("courseName", c.getCourseName());
		m.addAttribute("students", students);
		m.addAttribute("classParticipation", classParticipation);
		m.addAttribute("courseId", courseId);
		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(username,
						userdetails1.getProgramName()));
		/*List<String> cpWieghtageList = wieghtageDataService
				.showWieghtageForCP(courseId);
		logger.info("cpWieghtageList  " + cpWieghtageList);
		if (cpWieghtageList.size() == 0) {
			m.addAttribute("showWieghtageForCP", false);

		} else {
			m.addAttribute("showWieghtageForCP", true);
			m.addAttribute("cpWieghtage", cpWieghtageList.get(0));
		}*/
		return "classParticipation/classParticipation";
	}

	
	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadStudents", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String  DownloadStudents(
			@RequestParam Long courseId,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("DownloadStudents");
		
		Date date = new Date();
		File file = new File(downloadAllFolder + File.separator + "_" + date.getTime() + ".xlsx");
		try {
			
			
			file.createNewFile();
			List<ClassParticipation> students = classParticipationService
					.findStudentsForFaculty(courseId);
			System.out.println("students"+students);
			// List<ClassParticipation> savedStudents =
			// classParticipationService.findAllActive();

             
			
			Map<String, List<Map<String, Object>>> lstExcelData = new HashMap<String, List<Map<String, Object>>>();
			List<String> headers = new ArrayList<String>();
			headers.add("SAP ID");
			headers.add("STUDENT NAME");
			headers.add("ROLL NO");
			headers.add("CAMPUS NAME");
			headers.add("ASSIGNED SCORE");
			headers.add("ASSIGN REMARKS");
			
			
			List<Map<String, Object>> lst = new ArrayList<Map<String, Object>>();
			
			for (ClassParticipation uc : students) {
				
				Map<String, Object> map = new HashMap();
				User u = userService.findByUserName(uc.getUsername());

				//students.set(students.indexOf(uc), uc);
			

				System.out.println("uc.getUsername()"+uc.getUsername());
				
				map.put("SAP ID",uc.getUsername() );
				map.put("STUDENT NAME",uc.getFirstname()+uc.getLastname() );
				map.put("ROLL NO", u.getRollNo());
				map.put("CAMPUS NAME", u.getCampusName());
				map.put("ASSIGNED SCORE","");
				map.put("ASSIGN REMARKS", "");
			
				lst.add(map);
				

			}
			
			

			lstExcelData.put("Students", lst);
			ExcelCreater.createExcelFile(lstExcelData, headers,
					file.getAbsolutePath());

		} catch (Exception e) {
			logger.error("Exception ", e);
		}

		 file.getAbsolutePath();
	
		
		 
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		ServletContext context = request.getSession().getServletContext();
		File downloadFile = new File(file.getAbsolutePath());
		try {
			inputStream = new FileInputStream(downloadFile);
			outStream = response.getOutputStream();

			// get MIME type of the file
			String mimeType = context.getMimeType(file.getAbsolutePath());
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}
			logger.info("MIME type: " + mimeType);

			response.setContentLength((int) downloadFile.length());

			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					"StudentsMarks.xlsx");
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
	
	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/uploadStudentsMarks", method = { RequestMethod.POST })
	public String uploadStudentsMarks(@ModelAttribute ClassParticipation classParticipation,
			@RequestParam("file") MultipartFile file, Model m,
			RedirectAttributes redirectAttributes, Principal principal, Long courseId) {
		m.addAttribute("webPage", new WebPage("test", "Upload FAQs", true,
				false));
		logger.info("inside student marks : "+file.getOriginalFilename());
		
		System.out.println("classParticipationclassParticipationclassParticipationclassParticipation="+classParticipation.getCourseId());		
		List<String> validateHeaders = null;
		String username = principal.getName();

		validateHeaders = new ArrayList<String>(Arrays.asList("SAP ID",
				"STUDENT NAME", "ROLL NO","CAMPUS NAME","ASSIGNED SCORE","ASSIGN REMARKS"));

		ExcelReader excelReader = new ExcelReader();
		
		System.out.println("file uploaded");
			
		try {
			
			//Sandip 
			BusinessBypassRule.validateFile(classParticipation.getFile());
			
			List<Map<String, Object>> maps = excelReader.readExcelFileUsingColumnHeader(file, validateHeaders);
			System.out.println("mapsmapsmaps--------12222"+maps.get(0));
			System.out.println("mapsmapsmaps--------12222"+maps.get(1));
			
			//Sandip
			
			for (Map<String, Object> map : maps) {
				System.out.println("student username : " + map.get("SAP ID"));

				UserCourse classPart = userCourseService.checkIfStudentExistsInDB(map
						.get("SAP ID").toString(), Long.valueOf(classParticipation.getCourseId()));
				System.out.println("shshsh : " + classPart);

				if (null != classPart) {
					System.out.println("Students username is valid!");
				} else {
					throw new ValidationException("Invalid Students username!");
				}

			}
		   //Sandip
			
			
			logger.info("inside student marks : "+file.getOriginalFilename());
			if (maps.size() == 0) {
				setNote(m, "Excel File is empty");
				
			} 
			else {
				
				List<ClassParticipation> classparticipationsList =new ArrayList<ClassParticipation>();
				List<ClassParticipation> classparticipationsinList =new ArrayList<ClassParticipation>();
				
				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {
						
						setNote(m, "Error  " + mapper.get("Error"));
						
					} else {
					
						
						if (null==mapper.get("ASSIGNED SCORE").toString() ||mapper.get("ASSIGNED SCORE").toString().equals(" ")  || mapper.get("ASSIGNED SCORE").toString().isEmpty() || mapper.get("ASSIGNED SCORE").toString().matches("[a-zA-Z_]+") || Integer.parseInt((String) mapper.get("ASSIGNED SCORE")) < 0 )
						{
						
							setError(redirectAttributes, " check the marks entered for student Id "+mapper.get("SAP ID").toString());
							return "redirect:/classParticipation?courseId=" +classParticipation.getCourseId();
							
							
														
						}
						else {
							
							List<String> studentsFromDB = classParticipationService
							
									.findAllStudentUsernames(Long.valueOf(classParticipation.getCourseId()));
							
							
//							logger.info("List--->"+studentsFromDB);
//							logger.info("SAP ID--->"+mapper.get("SAP ID").toString());
//							logger.info("contains--->"+studentsFromDB.contains(mapper.get("SAP ID").toString()));
							ClassParticipation classparticipations = new ClassParticipation();
							if (studentsFromDB.contains(mapper.get("SAP ID").toString())) {
//								logger.info("SAP ID--->"+mapper.get("SAP ID").toString());
									classparticipations.setScore(Integer.parseInt(mapper.get("ASSIGNED SCORE").toString()));
									classparticipations.setRemarks(mapper.get("ASSIGN REMARKS").toString());
									classparticipations.setLastModifiedBy(username);
									classparticipations.setUsername(mapper.get("SAP ID").toString());
									classparticipations.setCourseId(classParticipation.getCourseId());
									classparticipationsList.add(classparticipations);
								
								
							} 
							
							else {
//								logger.info("class participation student ID -----> "+classparticipations.getUsername()); 
								classparticipations.setScore(Integer.parseInt(mapper.get("ASSIGNED SCORE").toString()));
								classparticipations.setRemarks(mapper.get("ASSIGN REMARKS").toString());
								classparticipations.setUsername(mapper.get("SAP ID").toString());
								classparticipations.setCreatedBy(username);
								classparticipations.setLastModifiedBy(username);
								classparticipations.setFacultyId(username);
								classparticipations.setCourseId(classParticipation.getCourseId());
								Course c = courseService.findByID(Long.valueOf(classParticipation.getCourseId()));
								classparticipations.setAcadMonth(c.getAcadMonth());
								classparticipations
										.setAcadYear(Integer.valueOf(c.getAcadYear()));
								classparticipationsinList.add(classparticipations);
								
													
							}
									
						}	
					}
				}
			
				classParticipationService.insertBatch(classparticipationsinList);
				
				
				classParticipationService.updateBatch(classparticipationsList);
				setSuccess(redirectAttributes, "Marks uploaded successfully");
				return "redirect:/classParticipation?courseId=" +classParticipation.getCourseId();
			}
			
		}
		catch (ValidationException er) { 
			
			logger.info("inside ValidationException er"+er.getMessage());
			setError(redirectAttributes,er.getMessage());
		}
		
		catch (Exception ex) {
			logger.info("inside ValidationException er"+ex.getMessage());
			setError(redirectAttributes, ex.getMessage());
			ex.printStackTrace();
		}
		return "redirect:/classParticipation?courseId=" +classParticipation.getCourseId();
	}
	
	@RequestMapping(value = "/saveClassParticipation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveClassParticipation(Model m,
			@RequestParam String studentUsername, @RequestParam String score,
			@RequestParam String remarks, @RequestParam String courseId,
			@ModelAttribute ClassParticipation classParticipation,
			Principal principal) {

		String username = principal.getName();
		List<String> studentsFromDB = classParticipationService
				.findAllStudentUsernames(Long.valueOf(courseId));
		

		try {
			
			/*****By sandip 25/10/2021******/
			
			System.out.println("saveClassParticipation course Id : "+courseId);
			 
			String s=String.valueOf(classParticipation.getScore());
			
			BusinessBypassRule.validateAlphaNumeric(classParticipation.getRemarks());
			
			BusinessBypassRule.validateNumericNotAZero(s);
	        /*****By sandip 25/10/2021******/
			
			if (studentsFromDB.contains(studentUsername)) {
				List<ClassParticipation> student = classParticipationService
						.findByStudent(studentUsername);
					
				
				for (ClassParticipation cp : student) {

					cp.setScore(Integer.valueOf(score));
					cp.setRemarks(remarks);
					classParticipationService.update(cp);
				}

			} else {

				classParticipation.setScore(Integer.valueOf(score));
				classParticipation.setRemarks(remarks);
				classParticipation.setUsername(studentUsername);
				classParticipation.setCreatedBy(username);
				classParticipation.setLastModifiedBy(username);
				classParticipation.setFacultyId(username);
				classParticipation.setCourseId(courseId);
				Course c = courseService.findByID(Long.valueOf(courseId));
				classParticipation.setAcadMonth(c.getAcadMonth());
				classParticipation
						.setAcadYear(Integer.valueOf(c.getAcadYear()));
				
				classParticipationService
						.insertWithIdReturn(classParticipation);
			}
			
			setSuccess(m, "Score saved successfully!");
			m.addAttribute("showIcon", false);
			//return "Success";
			
			String json = "{\"Status\":\"Success\"}";
			return json;
			
		}
		

		
		catch (ValidationException er) {
			// print the stack trace
			logger.error(er.getMessage(), er);
			setError(m,er.getMessage());
			String json = "{\"Status\":\"Error\", \"msg\":\""+er.getMessage()+"\"}";
			return json;
			}
			catch (Exception e) {
			logger.error("Error " + e);
			//return "Error";
			String json = "{\"Status\":\"Error\", \"msg\":\""+e.getMessage()+"\"}";
			return json;
			}
		
	}

}
