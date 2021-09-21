/*package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.wieghtage.WieghtageData;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.wieghtage.WieghtageDataService;
import com.spts.lms.services.wieghtage.WieghtageService;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;

@Controller
public class WieghtageController extends BaseController {

	@Autowired
	WieghtageService wieghtageService;

	@Autowired
	UserService userService;

	@Autowired
	WieghtageDataService wieghtageDataService;

	@Value("#{'${lms.weightageTypeList}'.split(',')}")
	private List<String> weightageTypeList;

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger
			.getLogger(WieghtageController.class);

	@RequestMapping(value = "/addAssignWieghtageForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addAssignWieghtageForm(WieghtageData wieghtageData, Model m,

	Principal p) {
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		String ProgramId = userdetails1.getProgramId();
		User u1 = userService.findByUserName(username);
		logger.info("ACAD SESSION------------------------->"
				+ u1.getAcadSession());

		String acadSession = u1.getAcadSession();
		logger.info("Program NAme--------------------> " + ProgramName);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("addAssignWieghtage",
				"Add Assign Wieghtage", false, false));
		m.addAttribute("courses",
				courseService.findByCoursesBasedOnProgramName(ProgramName));

		m.addAttribute("wieghtage", wieghtageData);
		return "course/internalWieghtage";
	}

	@RequestMapping(value = "/internalWieghtage", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addAssignWieghtage(
			@ModelAttribute WieghtageData wieghtageData, Model m, Principal p) {
		try {
			String username = p.getName();
			Token userdetails1 = (Token) p;
			String ProgramName = userdetails1.getProgramName();
			User u1 = userService.findByUserName(username);
			logger.info("ACAD SESSION------------------------->"
					+ u1.getAcadSession());

			String acadSession = u1.getAcadSession();
			logger.info("Program NAme--------------------> " + ProgramName);
			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			wieghtageData.setId(wieghtageData.getCourseId());
			m.addAttribute("courseId", wieghtageData.getCourseId());
			wieghtageDataService.insert(wieghtageData);
			setSuccess(m, "Successfully created Overview for course");
		} catch (Exception e) {
			setError(m, "Error in  creating Overview for course");
		}
		return addAssignWieghtageForm(wieghtageData, m, p);
	}

	@RequestMapping(value = "/saveInternalWieghtage", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveInternalWieghtage(Model m,
			@RequestParam String wieghtagetype,
			@RequestParam String wieghtageassigned,
			@RequestParam String courseId,
			@ModelAttribute WieghtageData wieghtageData, Principal principal) {

		String username = principal.getName();

		try {

			wieghtageData.setCreatedBy(username);
			wieghtageData.setLastModifiedBy(username);
			wieghtageData.setCourseId(Long.valueOf(courseId));
			wieghtageData.setWieghtageassigned(wieghtageassigned);
			wieghtageData.setWieghtagetype(wieghtagetype);
			Course c = courseService.findByID(Long.valueOf(courseId));

			logger.info("INSERTING DATA");
			wieghtageDataService.insertWithIdReturn(wieghtageData);

			setSuccess(m, "Score saved successfully!");
			m.addAttribute("showIcon", false);
			return "Success";
		} catch (Exception e) {

			logger.error("Error " + e.getMessage());
			return "Error";
		}

	}

	@RequestMapping(value = "/addInternalWieghtageForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addInternalWieghtageForm(
			@ModelAttribute WieghtageData wieghtageData,
			@RequestParam(required = false) Long courseId, Model m,
			Principal principal) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		logger.info("ACAD SESSION------------------------->"
				+ u1.getAcadSession());

		String acadSession = u1.getAcadSession();
		logger.info("Program NAme--------------------> " + ProgramName);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("addInternalWieghtage",
				"Add Wieghtage", false, false));
		List<WieghtageData> addedWieghtageList = wieghtageDataService
				.findWieghtageByCourse(courseId);
		m.addAttribute("addedWieghtageList", addedWieghtageList);
		logger.info("wieghatage data  in addInternal " + wieghtageData);
		m.addAttribute("courseId", courseId);
		Course c = courseService.findByID(courseId);
		m.addAttribute("courseName", c.getCourseName());
		List<WieghtageData> weightageDataList = new ArrayList<WieghtageData>();
		m.addAttribute("wieghtageData", wieghtageData);
		Integer i = 0;
		for (String s : weightageTypeList) {
			logger.info("SSSSSSS " + s);
			WieghtageData wd = new WieghtageData();
			List<WieghtageData> addedWeightage = wieghtageDataService
					.findAddedWeightageByCourseIdAndWeightageType(courseId, s);
			if (addedWeightage.size() == 0) {
				wd.setWieghtagetype(s);
				weightageDataList.add(wd);
				i++;
			} else {

			}

		}
		m.addAttribute("weightageDataList", weightageDataList);
		m.addAttribute("weightageTypeList", weightageTypeList);
		return "course/wieghtageData";
	}

	@RequestMapping(value = "/wieghtageData", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addSyllabus(@ModelAttribute WieghtageData wieghtageData,
			@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) String wieghtagetype,
			@RequestParam(required = false) String wieghtageassigned, Model m,
			Principal principal) {
		logger.info("wieghtageassigned--------" + wieghtageassigned);
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		logger.info("ACAD SESSION------------------------->"
				+ u1.getAcadSession());

		String acadSession = u1.getAcadSession();
		logger.info("Program NAme--------------------> " + ProgramName);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("addWieghtageData",
				"Add Wieghtage Data", false, false));

		String list1 = wieghtagetype;
		String[] values = list1.split(",");
		List<String> list2 = new ArrayList<String>(Arrays.asList(values));

		String list3 = wieghtageassigned;
		String[] values1 = list3.split(",");
		logger.info("values1---" + values1);
		List<String> list4 = new ArrayList<String>(Arrays.asList(values1));
		logger.info("list4---" + list4);
		wieghtageData.setCreatedBy(username);
		wieghtageData.setLastModifiedBy(username);
		wieghtageData.setCourseId(courseId);
		for (int i = 0; i <= list2.size() - 1 && i <= list4.size() - 1; i++) {
			wieghtageData.setWieghtageassigned(list4.get(i));
			wieghtageData.setWieghtagetype(list2.get(i));
			wieghtageDataService.insert(wieghtageData);
		}

		// return "course/wieghtageData";
		return "redirect:/addInternalWieghtageForm?courseId=" + courseId;
	}

	@RequestMapping(value = "/uploadWeightageData", method = { RequestMethod.POST })
	public String uploadWeightageData(
			@ModelAttribute WieghtageData wieghtageData,
			@RequestParam Long courseId,
			@RequestParam("file") MultipartFile file, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("test", "Upload Weightage Data",
				true, false));
		List<String> validateHeaders = null;
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		logger.info("ACAD SESSION------------------------->"
				+ u.getAcadSession());

		String acadSession = u.getAcadSession();
		logger.info("Program NAme--------------------> " + ProgramName);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		validateHeaders = new ArrayList<String>(Arrays.asList("wieghtagetype",
				"wieghtageassigned"));

		logger.info("validateHeaders" + validateHeaders);
		logger.info("validate header size" + validateHeaders.size());

		ExcelReader excelReader = new ExcelReader();

		try {
			List<Map<String, Object>> maps = excelReader
					.readExcelFileUsingColumnHeader(file, validateHeaders);
			logger.info("map size---->" + maps.size());

			if (maps.size() == 0) {
				setNote(m, "Excel File is empty");
			} else {

				Map<String, Object> map = maps.get(0);
				
				 * int firstQuestionanswer = Integer.parseInt((String) map
				 * .get("answer"));
				 

				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {
						logger.info("Error exist");
						setNote(m, "Error  " + mapper.get("Error"));
					} else {
						WieghtageData wD = new WieghtageData();

						wD.setCreatedBy(username);
						wD.setLastModifiedBy(username);
						wD.setCourseId(courseId);

						mapper.put("createdBy", wD.getCreatedBy());
						mapper.put("lastModifiedBy", wD.getLastModifiedBy());
						mapper.put("createdDate", Utils.getInIST());
						mapper.put("lastModifiedDate", Utils.getInIST());
						mapper.put("courseId", courseId);
						wieghtageDataService.insertUsingMap(mapper);
						setSuccess(redirectAttributes,
								"Weightage file uploaded successfully");

					}
				}

			}
			m.addAttribute("wieghtageData", wieghtageData);
		} catch (Exception ex) {
			setError(m, "Error in uploading file");
			ex.printStackTrace();
		}

		return "redirect:/viewWieghtageDetails?courseId=" + courseId;
	}

	@RequestMapping(value = "/viewWieghtageDetails", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewWieghtageDetails(
			@ModelAttribute WieghtageData wieghtageData, Model m,
			Principal principal, @RequestParam(required = false) Long courseId) {

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		logger.info("ACAD SESSION------------------------->"
				+ u1.getAcadSession());

		String acadSession = u1.getAcadSession();
		logger.info("Program NAme--------------------> " + ProgramName);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<WieghtageData> listOfAssignedWieghtage = wieghtageDataService
				.findWieghtageByCourse(courseId);
		m.addAttribute("coursesForViewWieghtage",
				courseService.findByCoursesBasedOnProgramName(ProgramName));
		m.addAttribute("wieghtageData", wieghtageData);
		m.addAttribute("listOfAssignedWieghtage", listOfAssignedWieghtage);
		return "course/viewWieghtage";
	}
}*/