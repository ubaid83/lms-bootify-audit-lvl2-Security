package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.expr.NewArray;

import org.apache.http.client.params.AllClientPNames;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.attendance.StudentCourseAttendance;
import com.spts.lms.beans.classParticipation.ClassParticipation;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.weight.Component;
import com.spts.lms.beans.weight.StudentWeightData;
import com.spts.lms.beans.weight.Weight;
import com.spts.lms.beans.weight.WeightData;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.helpers.excel.ReadWeightExcelFile;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.classParticipation.ClassParticipationService;
import com.spts.lms.services.content.ContentService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.forum.ForumReplyService;
import com.spts.lms.services.forum.ForumService;
import com.spts.lms.services.group.GroupService;
import com.spts.lms.services.group.StudentGroupService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.weight.ComponentService;
import com.spts.lms.services.weight.WeightDataService;
import com.spts.lms.services.weight.WeightService;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;

@Controller
public class WeightController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	UserService userService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	CourseService courseService;

	@Autowired
	WeightService weightService;

	@Autowired
	WeightDataService weightDataService;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	ContentService contentService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Autowired
	ClassParticipationService classParticipationService;

	@Autowired
	ForumService forumService;

	@Autowired
	ForumReplyService forumReplyService;

	@Autowired
	StudentGroupService studentGroupService;

	@Autowired
	TestService testService;

	@Autowired
	GroupService groupService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	ComponentService componentService;

	@Value("${lms.enrollment.years}")
	private String[] enrollmentYears;

	@Value("${workStoreDir:''}")
	private String workDir;

	@Value("#{'${lms.weightageTypeList}'.split(',')}")
	private List<String> weightageTypeList;

	@ModelAttribute("yearList")
	public String[] getYearList() {
		return enrollmentYears;
	}

	private static final Logger logger = Logger
			.getLogger(WeightController.class);

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addWeightForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addWeightForm(Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("weight", "Assign Weight", true,
				false));

		Weight weight = new Weight();
		m.addAttribute("weight", weight);
		return "weight/addWeight";

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addWeight", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addWeight(Model m, Principal principal,
			@ModelAttribute Weight weight) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("weight", "Assign Weight", true,
				false));
		String acadSessionForWeight = weight.getAcadSession();
		String acadYear = weight.getAcadYear();
		
		List<String> headers = new ArrayList<String>(
				Arrays.asList("courseName"));
		List<Course> allCourses = courseService
				.findByProgramIdAcadSessionAcadYear(
						Long.valueOf(userdetails1.getProgramId()), acadYear,
						acadSessionForWeight);
		weight.setAllCourses(allCourses);
		Boolean proceed = false;
		for (Course c : allCourses) {
			headers.add(c.getCourseName());
			List<WeightData> weightList = weightDataService.findByCourseIds(c
					.getId());
			if (weightList.size() != 0) {
				proceed = true;
				for (WeightData w : weightList) {
					weight.setInternal(w.getInternal());
					weight.setExternal(w.getExternal());
					c.setInternal(w.getInternal());
					c.setExternal(w.getExternal());
				}

			} else {
				proceed = false;

			}
			allCourses.set(allCourses.indexOf(c), c);
		}
		
		m.addAttribute("allCourses", allCourses);
		m.addAttribute("proceed", proceed);
		int i = 9;
		String sname = "TestSheet";

		XSSFWorkbook workbook;
		// File file = new File(type);
		FileOutputStream fos = null;

		List<String> weightageTypeList = weightService.findWeightType();

		try {
			File dest = new File(workDir + File.separator
					+ "weightTemplate.xlsx");
			String filePath = dest.getAbsolutePath();
			filePath = filePath.replaceAll("\\\\", "/");
			m.addAttribute("filePath", filePath);

			fos = new FileOutputStream(dest);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			logger.error("Exception", e1);
		}
		workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(sname);
		// XSSFSheet sheet1 = workbook.createSheet(sname1);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setLocked(true);
		// sheet.createFreezePane(0, 1);
		sheet.createFreezePane(1, 1, 1, 1);
		// sheet.createFreezePane(0, 0);

		try {
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("WeightType");
			for (int colNum = 1; colNum < headers.size(); colNum++) {
				Cell cell = headerRow.createCell(colNum);
				cell.setCellValue(headers.get(colNum));
				cell.setCellStyle(cellStyle);

			}

			for (String s : weightageTypeList) {
				Row weightRow = sheet.createRow(sheet.getLastRowNum() + 1);
				weightRow.createCell(0).setCellValue(s);
			}

			int rowNum = sheet.getLastRowNum();
			Row icaRow = sheet.createRow(rowNum + 1);
			icaRow.createCell(0).setCellValue("icaTotal");
			Row icaPassRow = sheet.createRow(rowNum + 2);
			icaPassRow.createCell(0).setCellValue("icaPassing");
			Row teeRow = sheet.createRow(rowNum + 3);
			teeRow.createCell(0).setCellValue("TEE");

			Row totalRow = sheet.createRow(rowNum + 4);
			totalRow.createCell(0).setCellValue("Total");

			workbook.write(fos);
			fos.flush();
			fos.close();

			
		} catch (Exception e) {
			logger.error("Exception while writing excel", e);
		}

		m.addAttribute("acadSession", acadSessionForWeight);
		m.addAttribute("acadYear", acadYear);

		m.addAttribute("weight", weight);
		return "weight/addWeight";

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/uploadWeight", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String uploadWeight(Model m, @ModelAttribute Weight weight,
			@RequestParam(required = false) String acadYear,
			@RequestParam(required = false) String acadSession,
			@RequestParam("file") MultipartFile file, Principal principal,
			RedirectAttributes redirectAttributes) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession1 = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession1);
		m.addAttribute("webPage", new WebPage("weight", "Assign Weight", true,
				false));
		/*
		 * String acadSessionForWeight = weight.getAcadSession(); String
		 * acadYear = weight.getAcadYear();
		 */
		
		List<Course> allCourses = courseService
				.findByProgramIdAcadSessionAcadYear(
						Long.valueOf(userdetails1.getProgramId()), acadYear,
						acadSession);
		

		List<String> validateHeaders = new ArrayList<String>();
		validateHeaders.add("WeightType");
		for (Course c : allCourses) {
			
			validateHeaders.add(c.getCourseName());
		}

		ExcelReader excelReader = new ExcelReader();

		try {

			ReadWeightExcelFile rwef = new ReadWeightExcelFile();
			List<List<String>> dataList = rwef.readWeightFile(file, allCourses);
			
			if (!dataList.isEmpty() || dataList.size() != 0) {

				int i = 1;
				for (Course c : allCourses) {

					for (List<String> list1 : dataList) {
						
						if (i < list1.size()) {
							if (list1.get(i) == null) {

							} else {
								
								if (weightDataService
										.findByWeightTypeAndCourseId(
												list1.get(0), c.getId()) != null) {
								} else {
									
									WeightData wd = new WeightData();
									wd.setCourseId(c.getId());
									wd.setCourseName(c.getCourseName());
									wd.setWeightType(list1.get(0));
									wd.setWeightAssigned(list1.get(i));
									wd.setCreatedBy(username);
									wd.setCreatedDate(Utils.getInIST());
									wd.setLastModifiedBy(username);
									weightDataService.insertWithIdReturn(wd);
								}

							}

						}
					}
					i++;

				}

			} else {
				setError(redirectAttributes, "Please check file format!");
				return "redirect:/addWeight";

			}

			m.addAttribute("weight", weight);
		} catch (Exception ex) {
			setError(redirectAttributes, "Error in uploading file");
			ex.printStackTrace();
		}

		return "redirect:/viewWieghtDetails";

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/viewWieghtDetails", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewWieghtDetails(Model m, Principal principal,
			@RequestParam(required = false) String acadSession,
			@RequestParam(required = false) String acadYear,
			@ModelAttribute Weight weight, RedirectAttributes redirectAttributes) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession1 = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession1);
		m.addAttribute("webPage", new WebPage("weight", "Assign Weight", true,
				false));
		
		List<Course> allCourses = new ArrayList<Course>();
		if (acadSession != null && acadYear != null) {
			allCourses = courseService.findByProgramIdAcadSessionAcadYear(
					Long.valueOf(userdetails1.getProgramId()), acadYear,
					acadSession);

		} else {
			allCourses = courseService
					.findByCoursesBasedOnProgramName(ProgramName);
		}
		
		if (allCourses.size() != 0) {
			List<List<WeightData>> weightDataList = new ArrayList<List<WeightData>>();
			Map<Long, List<WeightData>> weightMap = new HashMap<Long, List<WeightData>>();
			for (Course c : allCourses) {

				List<WeightData> weightList = weightDataService
						.findByCourseIds(c.getId());
				weightMap.put(c.getId(), weightList);

				allCourses.set(allCourses.indexOf(c), c);

			}
			
			m.addAttribute("weightMap", weightMap);
		} else {
			setNote(m, "No Courses Found!");
		}
		m.addAttribute("allCourses", allCourses);
		// m.addAttribute("weightDataList", weightDataList);

		return "weight/viewAssignedWeight";

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/generateGrades", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String generateGrades(Model m, Principal principal,
			@RequestParam Long courseId) {
		String username = principal.getName();

		m.addAttribute("courseId", courseId);
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		// logger.info("ACAD SESSION------------------------->"+
		// u.getAcadSession());

		String acadSession1 = u.getAcadSession();
		// 
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession1);
		m.addAttribute("webPage", new WebPage("viewAssignment", "View Grades",
				true, true));
		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(username,
						userdetails1.getProgramName()));
		StudentWeightData studentWeightData = new StudentWeightData();
		studentWeightData.setCourseId(String.valueOf(courseId));
		m.addAttribute("studentWeightData", studentWeightData);
		return "weight/generateGrades";

	}

	@Secured({"ROLE_STUDENT","ROLE_ADMIN"})
	@RequestMapping(value = "/viewGradeWeight", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewGradeWeight(Model m, Principal principal,
			@RequestParam Long courseId,
			@ModelAttribute StudentWeightData studentWeightData) {
		String username = principal.getName();
		
		String gradingType = studentWeightData.getGradingType();

		m.addAttribute("courseId", courseId);
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		// logger.info("ACAD SESSION------------------------->"+
		// u.getAcadSession());

		String acadSession1 = u.getAcadSession();
		// 
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession1);
		m.addAttribute("webPage", new WebPage("viewAssignment", "View Grades",
				true, true));
		List<UserCourse> students = userCourseService
				.findStudentsForFaculty(courseId);
		m.addAttribute("students", students);
		Map<String, String> assignedWeightMap = new HashMap<String, String>();
		List<WeightData> weightList = weightDataService
				.findByCourseIds(courseId);
		

		List<StudentWeightData> studentList = new ArrayList<StudentWeightData>();
		if (weightList.isEmpty() || weightList.size() == 0) {
			setNote(m, "Weight not assigned to this course");

		} else {
			for (WeightData wd : weightList) {
				assignedWeightMap.put(wd.getWeightType(),
						wd.getWeightAssigned());
			}

			for (UserCourse uc : students) {
				List<List<String>> dataList = new ArrayList<List<String>>();

				String sapId = uc.getUsername();
				
				/*
				 * List<Component> componentList =
				 * componentService.findByStudent( sapId,
				 * String.valueOf(courseId));
				 */

				List<StudentAssignment> studentAssignmentList = studentAssignmentService
						.findAssignmentByStudent(sapId,
								String.valueOf(courseId));
				
				List<StudentTest> studentTestList = studentTestService
						.findTestByStudent(sapId, String.valueOf(courseId));
				List<ClassParticipation> cpList = classParticipationService
						.findByStudent(sapId, String.valueOf(courseId));
				/*
				 * if (!componentList.isEmpty() || componentList.size() != 0) {
				 * for (Component c : componentList) { List<String> list = new
				 * ArrayList<String>(); if
				 * (assignedWeightMap.containsKey(c.getCompName())) {
				 * 
				 * list.add("Comp-" + c.getCompName());
				 * logger.info("Comp Score - " + c.getScore());
				 * list.add(c.getScore()); // dataList.add(list);
				 * 
				 * // if (list.size() != 0) { dataList.add(list); } }
				 * 
				 * }
				 */
				if (!studentAssignmentList.isEmpty()
						|| studentAssignmentList.size() != 0) {
					for (StudentAssignment sa : studentAssignmentList) {
						List<String> list = new ArrayList<String>();
						Long assignmentId = sa.getAssignmentId();

						Assignment a = assignmentService.findByID(assignmentId);

						list.add(a.getAssignmentType());
						list.add(a.getMaxScore());
						list.add(sa.getScore());
						// dataList.add(list);

						// if (list.size() != 0) {
						dataList.add(list);
						// }

					}
				}
				if (!cpList.isEmpty() || cpList.size() != 0) {

					for (ClassParticipation cp : cpList) {
						List<String> list = new ArrayList<String>();
						list.add("ClassParticipation");
						list.add(String.valueOf(cp.getScore()));
						dataList.add(list);

					}
				}
				if (assignedWeightMap.containsKey("Test")) {
					if (!studentTestList.isEmpty()
							|| studentTestList.size() != 0) {
						for (StudentTest st : studentTestList) {
							List<String> list = new ArrayList<String>();
							Long testId = st.getTestId();
							Test t = testService.findByID(testId);
							list.add("Test");
							list.add(String.valueOf(t.getMaxScore()));
							list.add(String.valueOf(st.getScore()));
							dataList.add(list);

						}
					}
				}
				
				Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
				for (List<String> list : dataList) {
					
					String weightType = "";
					if (list.get(0).startsWith("Comp")) {
						weightType = list.get(0).substring(5);
					} else {
						weightType = list.get(0);
					}
					List<String> marksList = new ArrayList<String>();
					WeightData weight = weightDataService
							.findByWeightTypeAndCourseId(weightType, courseId);
					

					if (dataMap.isEmpty() || !dataMap.containsKey(list.get(0))) {
						
						marksList.add(list.get(1));
						/*
						 * if
						 * (!list.get(0).equalsIgnoreCase("ClassParticipation")
						 * || !list.get(0).contains("Comp-")) {
						 * marksList.add(list.get(2)); }
						 */
						if (list.size() > 2) {
							marksList.add(list.get(2));
						}
						
						marksList.add("W" + weight.getWeightAssigned());

						dataMap.put(list.get(0), marksList);
					} else {
						
						if (dataMap.containsKey(list.get(0))) {
							

							marksList.add(list.get(1));
							marksList.add(list.get(2));
							marksList.addAll(dataMap.get(list.get(0)));
							dataMap.replace(list.get(0), marksList);

						} else {

						}
					}
					// i++;

				}

				
				double score = 0.0;

				for (String s : dataMap.keySet()) {
					double temp = 0.0;
					
					double outOfMarks = 0.0;
					double scoredMarks = 0.0;
					double marks = 0.0;
					int count = 0;
					String weight = null;
					List<String> values = dataMap.get(s);
					
					for (int i = 0; i <= values.size(); i = i + 2) {
						weight = values.get(values.size() - 1);
						
						if (studentWeightData.getGradingType()
								.equalsIgnoreCase("Average")) {
							
							if (i != values.size() - 1) {
								/*
								 * logger.info("Values - i ----------------------"
								 * + values.get(i));
								 */
								if (s.equalsIgnoreCase("ClassParticipation")
										|| s.startsWith("Comp-")) {
									scoredMarks = Double.parseDouble(values
											.get(0));
									count = 1;
								} else {
									/*
									 * outOfMarks = Double.parseDouble(values
									 * .get(i)) + outOfMarks; //
									 * logger.info("outOfMarks - " + //
									 * outOfMarks); if (values.get(i + 1) !=
									 * null) { scoredMarks =
									 * Double.parseDouble(values .get(i + 1)) +
									 * scoredMarks; } else { scoredMarks = 0.0 +
									 * scoredMarks; }
									 */

									outOfMarks = Double.parseDouble(values
											.get(i));
									if (values.get(i + 1) != null) {
										scoredMarks = Double.parseDouble(values
												.get(i + 1));
										marks = (scoredMarks / outOfMarks)
												+ marks;
									} else {
										scoredMarks = 0.0;
									}

									count++;
								}

							}
						} else {
							
							if (i != values.size() - 1) {
								if (s.equalsIgnoreCase("ClassParticipation")
										|| s.startsWith("Comp-")) {
									scoredMarks = Double.parseDouble(values
											.get(0));
									count = 1;
								} else {
									double temp1 = 0.0;
									if (values.get(i + 1) != null) {
										temp1 = (Double.parseDouble(values
												.get(i + 1)) / Double
												.parseDouble(values.get(i))) * 100;
									}
									if (temp1 > temp) {
										temp = temp1;
										outOfMarks = Double.parseDouble(values
												.get(i));
										scoredMarks = Double.parseDouble(values
												.get(i + 1));
										marks = (scoredMarks/outOfMarks)+marks;
									}

									count++;
								}

							}
						}

					}
					
					marks = marks / count;
					
					outOfMarks = outOfMarks / count;
					scoredMarks = scoredMarks / count;
					
					if (s.equals("ClassParticipation") || s.startsWith("Comp-")) {
						scoredMarks = (Double.parseDouble(weight.substring(1)) * scoredMarks)
								/ (Double.parseDouble(weight.substring(1)));
					} else {
						/*scoredMarks = (Double.parseDouble(weight.substring(1)) * scoredMarks)
								/ outOfMarks;*/
						scoredMarks = (Double.parseDouble(weight.substring(1)) * marks);
					}
					
					score = score + scoredMarks;

				}
				
				double valueRounded = Math.round(score * 100D) / 100D;
				StudentWeightData student = new StudentWeightData();
				student.setUsername(sapId);
				student.setIcaTotal(assignedWeightMap.get("icaTotal"));
				student.setIcaPass(assignedWeightMap.get("Pass ICA"));
				student.setIcaScored(String.valueOf(valueRounded));
				student.setTee(assignedWeightMap.get("TEE"));
				student.setTotal(assignedWeightMap.get("Total"));
				
				if (valueRounded < Double.parseDouble(assignedWeightMap
						.get("icaPassing"))) {
					student.setIcaPassed(false);
				} else {
					student.setIcaPassed(true);
				}
				student.setFirstname(uc.getFirstname());
				student.setLastname(uc.getLastname());
				student.setRollNo(uc.getRollNo());
				studentList.add(student);

			}
		}
		m.addAttribute("studentList", studentList);
		// StudentWeightData studentWeightData = new StudentWeightData();
		studentWeightData.setCourseId(String.valueOf(courseId));
		Course c = courseService.findByID(courseId);
		m.addAttribute("courseName", c.getCourseName());
		m.addAttribute("studentWeightData", studentWeightData);
		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(username,
						userdetails1.getProgramName()));

		return "weight/viewGradeWeight";

	}

	@Secured({"ROLE_STUDENT"})
	@RequestMapping(value = "/viewGradeWeightForStudent", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewGradeWeightForStudent(Model m, Principal principal,
			@RequestParam Long courseId) {
		String username = principal.getName();

		m.addAttribute("courseId", courseId);
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		// logger.info("ACAD SESSION------------------------->"+
		// u.getAcadSession());

		String acadSession1 = u.getAcadSession();
		// 
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession1);
		m.addAttribute("webPage", new WebPage("viewAssignment", "View Grades",
				true, true));
		/*
		 * List<UserCourse> students = userCourseService
		 * .findStudentsForFaculty(courseId); m.addAttribute("students",
		 * students);
		 */
		Map<String, String> assignedWeightMap = new HashMap<String, String>();
		List<WeightData> weightList = weightDataService
				.findByCourseIds(courseId);
		

		List<WeightData> weightDataList = new ArrayList<WeightData>();

		List<StudentWeightData> studentList = new ArrayList<StudentWeightData>();
		if (weightList.isEmpty() || weightList.size() == 0) {
			setNote(m, "Weight not assigned to this course");

		} else {
			for (WeightData wd : weightList) {
				assignedWeightMap.put(wd.getWeightType(),
						wd.getWeightAssigned());
			}

			// for (UserCourse uc : students) {
			List<List<String>> dataList = new ArrayList<List<String>>();
			// String sapId = uc.getUsername();
			/*
			 * List<Component> componentList = componentService.findByStudent(
			 * username, String.valueOf(courseId));
			 */

			List<StudentAssignment> studentAssignmentList = studentAssignmentService
					.findAssignmentByStudent(username, String.valueOf(courseId));
			
			List<StudentTest> studentTestList = studentTestService
					.findTestByStudent(username, String.valueOf(courseId));
			List<ClassParticipation> cpList = classParticipationService
					.findByStudent(username, String.valueOf(courseId));
			/*
			 * if (!componentList.isEmpty() || componentList.size() != 0) { for
			 * (Component c : componentList) { List<String> list = new
			 * ArrayList<String>(); if
			 * (assignedWeightMap.containsKey(c.getCompName())) {
			 * 
			 * list.add("Comp-" + c.getCompName()); logger.info("Comp Score - "
			 * + c.getScore()); list.add(c.getScore()); // dataList.add(list);
			 * 
			 * // if (list.size() != 0) { dataList.add(list); } }
			 * 
			 * }
			 */
			for (StudentAssignment sa : studentAssignmentList) {
				List<String> list = new ArrayList<String>();
				Long assignmentId = sa.getAssignmentId();

				Assignment a = assignmentService.findByID(assignmentId);

				list.add(a.getAssignmentType());
				list.add(a.getMaxScore());
				list.add(sa.getScore());
				// dataList.add(list);

				// if (list.size() != 0) {
				dataList.add(list);
				// }

			}
			if (assignedWeightMap.containsKey("Test")) {
				for (StudentTest st : studentTestList) {
					List<String> list = new ArrayList<String>();
					Long testId = st.getTestId();
					Test t = testService.findByID(testId);
					list.add("Test");
					list.add(String.valueOf(t.getMaxScore()));
					list.add(String.valueOf(st.getScore()));
					dataList.add(list);

				}
			}

			for (ClassParticipation cp : cpList) {
				List<String> list = new ArrayList<String>();
				list.add("ClassParticipation");
				list.add(String.valueOf(cp.getScore()));
				dataList.add(list);

			}
			
			Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
			for (List<String> list : dataList) {
				
				String weightType = "";
				if (list.get(0).startsWith("Comp")) {
					weightType = list.get(0).substring(5);
				} else {
					weightType = list.get(0);
				}
				List<String> marksList = new ArrayList<String>();
				WeightData weight = weightDataService
						.findByWeightTypeAndCourseId(weightType, courseId);

				if (dataMap.isEmpty() || !dataMap.containsKey(list.get(0))) {
					
					marksList.add(list.get(1));
					if (list.size() > 2) {
						marksList.add(list.get(2));
					}
					marksList.add("W" + weight.getWeightAssigned());

					dataMap.put(list.get(0), marksList);
				} else {
					
					if (dataMap.containsKey(list.get(0))) {
						

						marksList.add(list.get(1));
						marksList.add(list.get(2));
						marksList.addAll(dataMap.get(list.get(0)));
						dataMap.replace(list.get(0), marksList);

					} else {

					}
				}
				// i++;

			}

			
			double score = 0.0;

			for (String s : dataMap.keySet()) {
				WeightData weightData = new WeightData();
				
				weightData.setWeightType(s);
				double outOfMarks = 0.0;
				double scoredMarks = 0.0;
				double marks = 0.0;
				int count = 0;
				String weight = null;
				List<String> values = dataMap.get(s);
				for (int i = 0; i <= values.size(); i = i + 2) {
					weight = values.get(values.size() - 1);
					
					if (i != values.size() - 1) {
						if (s.equalsIgnoreCase("ClassParticipation")
								|| s.startsWith("Comp-")) {
							scoredMarks = Double.parseDouble(values.get(0));
							count = 1;
						} else {
							/*
							 * outOfMarks = Double.parseDouble(values.get(i)) +
							 * outOfMarks; //outOfMarks =
							 * Double.parseDouble(values.get(i)); //
							 * logger.info("outOfMarks - " + outOfMarks);
							 * scoredMarks = Double.parseDouble(values.get(i +
							 * 1)) + scoredMarks; //scoredMarks =
							 * Double.parseDouble(values.get(i + 1));
							 */

							outOfMarks = Double.parseDouble(values.get(i));

							scoredMarks = Double.parseDouble(values.get(i + 1));
							marks = (scoredMarks / outOfMarks) + marks;

							// scoredMarks = Double.parseDouble(values.get(i +
							// 1));

							count++;
						}

					}

				}

				
				marks = marks / count;
				
				outOfMarks = outOfMarks / count;
				scoredMarks = scoredMarks / count;
				
				if (s.equals("ClassParticipation") || s.startsWith("Comp-")) {

					scoredMarks = (Double.parseDouble(weight.substring(1)) * scoredMarks)
							/ (Double.parseDouble(weight.substring(1)));
				} else {
					/*
					 * scoredMarks = (Double.parseDouble(weight.substring(1)) *
					 * scoredMarks) / outOfMarks;
					 */
					scoredMarks = (Double.parseDouble(weight.substring(1)) * marks);
				}
				
				score = score + scoredMarks;
				weightData.setWeightAssigned(weight.substring(1));
				weightData.setSemiFinalScore(String.valueOf(scoredMarks));
				weightData.setCount(String.valueOf(count));
				weightDataList.add(weightData);

			}
			m.addAttribute("weightDataList", weightDataList);

			
			// logger.info("SAPID - " + sapId);
			double valueRounded = Math.round(score * 100D) / 100D;
			StudentWeightData student = new StudentWeightData();
			student.setUsername(username);
			student.setIcaTotal(assignedWeightMap.get("icaTotal"));
			student.setIcaPass(assignedWeightMap.get("icaPassing"));
			student.setIcaScored(String.valueOf(valueRounded));
			student.setTee(assignedWeightMap.get("TEE"));
			student.setTotal(assignedWeightMap.get("Total"));
			if (valueRounded < Double.parseDouble(assignedWeightMap
					.get("icaPassing"))) {
				student.setIcaPassed(false);
			} else {
				student.setIcaPassed(true);
			}
			User uc = userService.findByUserName(username);
			student.setFirstname(uc.getFirstname());
			student.setLastname(uc.getLastname());
			student.setRollNo(uc.getRollNo());
			studentList.add(student);

		}
		// }
		m.addAttribute("studentList", studentList);
		StudentWeightData studentWeightData = new StudentWeightData();
		studentWeightData.setCourseId(String.valueOf(courseId));
		m.addAttribute("studentWeightData", studentWeightData);
		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(username,
						userdetails1.getProgramName()));

		return "weight/viewGradeWeightForStudent";

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN","ROLE_DEAN","ROLE_HOD","ROLE_STUDENT"})
	@RequestMapping(value = "/viewComponents", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewComponents(Principal principal, Model m,
			@RequestParam String courseId) {
		String username = principal.getName();

		m.addAttribute("courseId", courseId);
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		// logger.info("ACAD SESSION------------------------->"+
		// u.getAcadSession());

		String acadSession1 = u.getAcadSession();
		// 
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession1);
		m.addAttribute("webPage", new WebPage("viewAssignment", "View Grades",
				true, true));
		Component comp = new Component();
		comp.setCourseId(String.valueOf(courseId));
		m.addAttribute("comp", comp);
		List<String> compList = componentService
				.findCompNamesByCourseId(courseId);
		

		m.addAttribute("compList", compList);
		List<Course> allCourses = new ArrayList<Course>();
		if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)
				|| userdetails1.getAuthorities().contains(Role.ROLE_STUDENT)
				|| userdetails1.getAuthorities().contains(Role.ROLE_DEAN)
				|| userdetails1.getAuthorities().contains(Role.ROLE_HOD)) {
			allCourses = courseService.findByUserActive(username,
					userdetails1.getProgramName());
			m.addAttribute("allCourses", allCourses);
		}
		

		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			String progName = userdetails1.getProgramName();
			allCourses = courseService
					.findByCoursesBasedOnProgramName(progName);
			m.addAttribute("allCourses", allCourses);
		}
		return "weight/viewComponents";
	}

	@RequestMapping(value = "/addYourComponent", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addYourComponent(Principal principal, Model m,
			@RequestParam Long courseId) {
		String username = principal.getName();

		m.addAttribute("courseId", courseId);
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		// logger.info("ACAD SESSION------------------------->"+
		// u.getAcadSession());

		String acadSession1 = u.getAcadSession();
		// 
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession1);
		m.addAttribute("webPage", new WebPage("addComponent", "Add Component",
				true, true));
		List<String> weightTypeList = weightService.findWeightType();
		m.addAttribute("weightTypeList", weightTypeList);
		Component comp = new Component();
		comp.setCourseId(String.valueOf(courseId));
		m.addAttribute("comp", comp);
		List<Component> students = componentService
				.findStudentsForFaculty(courseId);

		for (Component uc : students) {
			User u1 = userService.findByUserName(uc.getUsername());
			uc.setRollNo(u1.getRollNo());
			students.set(students.indexOf(uc), uc);
		}

		
		Course c = courseService.findByID(courseId);
		m.addAttribute("courseName", c.getCourseName());
		m.addAttribute("students", students);
		return "weight/addYourComponent";

	}

	@RequestMapping(value = "/addComponent", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addComponent(Principal principal, Model m,
			@RequestParam Long courseId, @RequestParam String compName) {
		String username = principal.getName();

		m.addAttribute("courseId", courseId);
		m.addAttribute("compName", compName);
		
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		// logger.info("ACAD SESSION------------------------->"+
		// u.getAcadSession());

		String acadSession1 = u.getAcadSession();
		// 
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession1);
		m.addAttribute("webPage", new WebPage("addComponent", "Add Component",
				true, true));
		List<String> weightTypeList = weightService.findWeightType();
		m.addAttribute("weightTypeList", weightTypeList);
		Component comp = new Component();
		comp.setCourseId(String.valueOf(courseId));
		comp.setCompName(compName);
		m.addAttribute("comp", comp);
		List<Component> students = componentService.findStudentsForCompnent(
				courseId, compName);

		for (Component uc : students) {
			User u1 = userService.findByUserName(uc.getUsername());
			uc.setRollNo(u1.getRollNo());
			students.set(students.indexOf(uc), uc);
		}

		
		Course c = courseService.findByID(courseId);
		m.addAttribute("courseName", c.getCourseName());
		m.addAttribute("students", students);
		return "weight/addYourComponent";

	}

	@RequestMapping(value = "/saveStudentComponent", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentComponent(@ModelAttribute Component comp, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("addComponent", "Add Component",
				true, true));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		
		String compName = "";
		if (comp.getCompName().contains(",")) {
			compName = comp.getCompName().substring(
					comp.getCompName().lastIndexOf(",") + 1);

			
		} else {
			compName = comp.getCompName();
		}

		/*
		 * List<String> statusList = new ArrayList<String>();
		 * statusList.add("Present"); statusList.add("Absent");
		 */
		ArrayList<Component> studentMappingList = new ArrayList<Component>();
		List<String> studentList = new ArrayList<String>();
		List<Component> students = componentService.findStudentsForFaculty(Long
				.valueOf(comp.getCourseId()));
		// String[] statusList = attendance.getStatus().split(",");
		// String[] reasonList = attendance.getReason().split(",");
		// logger.info("Reason List ----- "+reasonList.toString());
		// logger.info("REason list size ---- "+reasonList.length);

		List<String> scoreList = new ArrayList<String>(Arrays.asList(comp
				.getScore().split(",")));
		List<String> remarkList = new ArrayList<String>(Arrays.asList(comp
				.getRemarks().split(",")));
		

		try {
			if (students != null && students.size() > 0) {
				int i = 0;
				for (Component sca : students) {
					Component bean = new Component();
					bean.setCompName(compName);

					bean.setCourseId(comp.getCourseId());
					bean.setUsername(sca.getUsername());
					bean.setFacultyId(username);
					bean.setCreatedBy(username);
					bean.setLastModifiedBy(username);
					bean.setRollNo(sca.getRollNo());
					bean.setRemarks(remarkList.get(i));
					bean.setScore(scoreList.get(i));
					bean.setActive("Y");
					Component checkBean = componentService
							.findByCourseAndStudentAndComp(comp.getCourseId(),
									sca.getUsername(), compName);
					if (checkBean == null) {

						studentMappingList.add(bean);
					} else {
						bean.setId(checkBean.getId());
						componentService.update(bean);
					}
					i++;

				}

				

				componentService.insertBatch(studentMappingList);
				

				setSuccess(redirectAttributes,
						"Details added for " + students.size()
								+ " students successfully");

				

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttributes, "Error in marking attendance");

		}
		m.addAttribute("comp", comp);
		return "redirect:/addComponent?courseId=" + comp.getCourseId()
				+ "&compName=" + compName;
	}

	@RequestMapping(value = "/deleteComponent", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String deleteComponent(Model m, @RequestParam String courseId,
			@RequestParam String compName, Principal principal,
			RedirectAttributes redirectAttributes) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		componentService.deleteComponent(courseId, compName);
		setSuccess(redirectAttributes, compName + "deleted successfully!");
		return "redirect:/viewComponents?courseId=" + courseId;

	}

}
